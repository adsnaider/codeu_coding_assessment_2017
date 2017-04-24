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

import java.io.IOException;

final class MyJSONParser implements JSONParser {

  private static class StringAndIndex {
    final String value;
    final int index;
    StringAndIndex(String value, int index) {
      this.value = value;
      this.index = index;
    }
  }

  private static class JSONAndIndex {
    final JSON value;
    final int index;
    JSONAndIndex(JSON value, int index) {
      this.value = value;
      this.index = index;
    }
  }

  @Override
  public JSON parse(String in) throws IOException {
    JSONAndIndex result = parseObject(in, 0);
    if (result.index != in.length()) {
      throw new IOException();
    }
    return result.value;
  }

  private JSONAndIndex parseObject(String in, int index) throws IOException {
    JSON json = new MyJSON();
    index = eatWhitespace(in, index);
    // "{"
    index = eatWhitespace(in, expectedToken(in, index, '{'));
    if (in.charAt(index) == '}') {
      return new JSONAndIndex(json, eatWhitespace(in, ++index));
    }
    while (index < in.length()) {
      // "Name"
      StringAndIndex name = parseString(in, index);
      index = eatWhitespace(in, name.index);

      // ":"
      index = eatWhitespace(in, expectedToken(in, index, ':'));
      if (in.charAt(index) == '\"') {
        StringAndIndex value = parseString(in, index);
        index = eatWhitespace(in, value.index);
        json.setString(name.value, value.value);
      } else if (in.charAt(index) == '{') {
        JSONAndIndex value = parseObject(in, index);
        index = eatWhitespace(in, value.index);
        json.setObject(name.value, value.value);
      } else {
        throw new IOException();
      }

      // "," or "}"
      if (in.charAt(index) == ',') {
        index = eatWhitespace(in, ++index);
        continue;
      } else if (in.charAt(index) == '}') {
        index = eatWhitespace(in, ++index);
        break;
      } else {
        throw new IOException();
      }
    }
    return new JSONAndIndex(json, index);
  }

  private StringAndIndex parseString(String in, int index) throws IOException {
    index = expectedToken(in, index, '\"');
    StringBuilder result = new StringBuilder();
    while (index < in.length()) {
      char c = in.charAt(index++);
      if (c == '\\') {
        c = in.charAt(index++);
        switch (c) {
          case '\\':
            result.append('\\');
            break;
          case '\"':
            result.append('\"');
            break;
          case '\n':
            result.append('\n');
            break;
          case '\t':
            result.append('\t');
            break;
          default:
            throw new IOException();
        }
      } else if (c == '\"') {
        return new StringAndIndex(result.toString(), index);
      } else {
        result.append(c);
      }
    }
    // No final quotation mark was found.
    throw new IOException();
  }

  private int expectedToken(String in, int index, char expected) throws IOException {
    if (in.charAt(index) != expected) {
      throw new IOException();
    }
    return ++index;
  }

  private int eatWhitespace(String s, int index) {
    while (index < s.length()) {
      char c = s.charAt(index);
      if (c == '\t' || c == '\n' || c == ' ') {
        index++;
      } else {
        break;
      }
    }
    return index;
  }
}

