/*
 * Copyright 2017 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.q

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.CLASS

/**
 * Implemented by all messages used with the [Queue]. Sub-types should be simple
 * immutable value types such as Kotlin data classes or _Lombok_ `@Value`
 * classes.
 */
@JsonTypeInfo(use = CLASS) abstract class Message {
  // TODO: this type should be immutable
  val attributes: MutableList<Attribute> = mutableListOf()

  /**
   * @return the attribute of type [A] or `null`.
   */
  inline fun <reified A : Attribute> getAttribute() =
    attributes.find { it is A } as A?

  /**
   * Adds an attribute of type [A] to the message.
   */
  inline fun <reified A: Attribute> setAttribute(attribute: A): A {
    attributes.removeIf { it is A }
    attributes.add(attribute)

    return attribute
  }
}

/**
 * The base type for message metadata attributes.
 */
@JsonTypeInfo(use = CLASS)
interface Attribute

/**
 * An attribute representing the maximum number of retries for a message.
 */
data class MaxAttemptsAttribute(val maxAttempts: Int = -1) : Attribute

/**
 * An attribute representing the number of times a message has been retried.
 */
data class AttemptsAttribute(var attempts: Int = 0) : Attribute
