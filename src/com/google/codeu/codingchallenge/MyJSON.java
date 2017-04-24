// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.codingchallenge;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// WARNING: this class is not multithread safe.
final class MyJSON implements JSON {
  private Map<String, String> strings = new HashMap<>();
  private Map<String, JSON> objects = new HashMap<>();

  @Override
  public JSON getObject(String name) {
    return objects.get(name);
  }

  @Override
  public JSON setObject(String name, JSON value) {
    strings.remove(name);
    objects.remove(name);
    objects.put(name, value);
    return this;
  }

  @Override
  public String getString(String name) {
    return strings.get(name);
  }

  @Override
  public JSON setString(String name, String value) {
    strings.remove(name);
    objects.remove(name);
    strings.put(name, value);
    return this;
  }

  @Override
  public void getObjects(Collection<String> names) {
    names.addAll(objects.keySet());
  }

  @Override
  public void getStrings(Collection<String> names) {
    names.addAll(strings.keySet());
  }
}
