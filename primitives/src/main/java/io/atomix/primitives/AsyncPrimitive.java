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
package io.atomix.primitives;

import java.util.concurrent.CompletableFuture;

/**
 * Asynchronous primitive.
 */
public interface AsyncPrimitive extends DistributedPrimitive {

  /**
   * Purges state associated with this primitive.
   * <p>
   * Implementations can override and provide appropriate clean up logic for purging
   * any state state associated with the primitive. Whether modifications made within the
   * destroy method have local or global visibility is left unspecified.
   *
   * @return {@code CompletableFuture} that is completed when the operation completes
   */
  default CompletableFuture<Void> destroy() {
    return CompletableFuture.completedFuture(null);
  }

  /**
   * Closes the primitive.
   *
   * @return a future to be completed once the primitive is closed
   */
  CompletableFuture<Void> close();

}
