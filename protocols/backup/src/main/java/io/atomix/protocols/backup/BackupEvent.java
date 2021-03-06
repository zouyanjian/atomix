/*
 * Copyright 2017-present Open Networking Foundation
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
 * limitations under the License.
 */
package io.atomix.protocols.backup;

import io.atomix.event.AbstractEvent;

/**
 * Backup event.
 */
public class BackupEvent<T> extends AbstractEvent<BackupEvent.Type, T> {

  /**
   * Failure detection event type.
   */
  public enum Type {
    BACKUP,
  }

  public BackupEvent(Type type, T subject) {
    super(type, subject);
  }

  public BackupEvent(Type type, T subject, long time) {
    super(type, subject, time);
  }
}
