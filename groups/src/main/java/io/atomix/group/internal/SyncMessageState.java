/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package io.atomix.group.internal;

import io.atomix.copycat.server.Commit;
import io.atomix.group.messaging.MessageProducer;

import java.util.Random;

/**
 * Synchronous message state.
 *
 * @author <a href="http://github.com/kuujo>Jordan Halterman</a>
 */
class SyncMessageState extends MessageState {
  private int members;
  private int ack;
  private int fail;

  public SyncMessageState(Commit<GroupCommands.Send> commit, QueueState queue) {
    super(commit, queue);
  }

  @Override
  public boolean send(MembersState members) {
    if (commit.operation().member() != null) {
      MemberState member = members.get(commit.operation().member());
      if (member != null) {
        member.submit(this);
        return true;
      } else {
        sendReply(false);
        return false;
      }
    } else if (commit.operation().dispatchPolicy() == MessageProducer.DispatchPolicy.RANDOM) {
      if (members.isEmpty()) {
        sendReply(false);
        return false;
      } else {
        members.get(new Random(commit.operation().id()).nextInt(members.size())).submit(this);
        return true;
      }
    } else if (commit.operation().dispatchPolicy() == MessageProducer.DispatchPolicy.BROADCAST) {
      this.members = members.size();
      members.forEach(m -> m.submit(this));
      return true;
    } else {
      sendReply(false);
      return false;
    }
  }

  @Override
  public void reply(Object message) {
    if ((Boolean) message) {
      ack++;
    } else {
      fail++;
    }

    if (ack + fail == members) {
      if (fail == 0) {
        sendReply(true);
      } else {
        sendReply(false);
      }
      queue.close(this);
    }
  }

  @Override
  public void expire() {
    fail++;
    if (ack + fail == members) {
      sendReply(false);
      queue.close(this);
    }
  }

}
