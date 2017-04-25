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
import java.util.HashSet;

import java.io.IOException;

final class TestMain {

  public static void main(String[] args) {

    final Tester tests = new Tester();

    tests.add("Empty Object", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {
        final JSONParser parser = factory.parser();
        final JSON obj = parser.parse("{ }");

        final Collection<String> strings = new HashSet<>();
        obj.getStrings(strings);

        Asserts.isEqual(strings.size(), 0);

        final Collection<String> objects = new HashSet<>();
        obj.getObjects(objects);

        Asserts.isEqual(objects.size(), 0);
      }
    });

    tests.add("Whitespace Only", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {
        final JSONParser parser = factory.parser();
        try {
          parser.parse(" \t  ");
        } catch (IOException ex) {
          // expected
        }
      }
    });

    tests.add("String Value", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {
        final JSONParser parser = factory.parser();
        final JSON obj = parser.parse("{ \"name\":\"sam doe\" }");

        Asserts.isEqual("sam doe", obj.getString("name"));
     }
    });

    tests.add("Double value error", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {
        final JSONParser parser = factory.parser();
        try {
          parser.parse("{ \"name\":\"sam doe\" \"error\"}");
          Asserts.isTrue(false, "Exception should be caught");

        } catch (IOException ex) {
          // expected
        }
      }
    });

    tests.add("Object Value", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {

        final JSONParser parser = factory.parser();
        final JSON obj = parser.parse("{ \"name\":{\"first\":\"sam\", \"last\":\"doe\" } }");

        final JSON nameObj = obj.getObject("name");

        Asserts.isNotNull(nameObj);
        Asserts.isEqual("sam", nameObj.getString("first"));
        Asserts.isEqual("doe", nameObj.getString("last"));
      }
    });

    tests.add("Object Value extra comma", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {

        final JSONParser parser = factory.parser();
        try {
          parser.parse("{ \"name\":{\"first\":\"sam\", \"last\":\"doe\", } }");
        } catch (IOException ex) {
          // expected
        }
      }
    });

    tests.add("Object Value Extra Bracket", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {

        final JSONParser parser = factory.parser();
        try {
          parser.parse("{ \"name\":{{\"first\":\"sam\", \"last\":\"doe\" } }");
        } catch (IOException ex) {
          // expected
        }
      }
    });

    tests.add("Object Value extra colon", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {

        final JSONParser parser = factory.parser();
        try {
          parser.parse("{ \"name\":{\"first\"::\"sam\", \"last\":\"doe\" } }");
        } catch (IOException ex) {
          // expected
        }
      }
    });

    tests.add("Object Value extra closing bracket", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {

        final JSONParser parser = factory.parser();
        try {
          parser.parse("{ \"name\":{\"first\":\"sam\", \"last\":\"doe\" }} }");
        } catch (IOException ex) {
          // expected
        }
      }
    });

    tests.add("Object Value missing closing bracket", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {

        final JSONParser parser = factory.parser();
        try {
          parser.parse("{ \"name\":{\"first\":\"sam\", \"last\":\"doe\"  }");
        } catch (IOException ex) {
          // expected
        }
      }
    });

    tests.add("Object Value single bracket", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {

        final JSONParser parser = factory.parser();
        try {
          parser.parse("{");
        } catch (IOException ex) {
          // expected
        }
      }
    });

    tests.add("Object Value Missing Quotation mark", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {

        final JSONParser parser = factory.parser();
        try {
          parser.parse("{ \"name\":{\"first:\"sam\", \"last\":\"doe\" } }");
        } catch (IOException ex) {
          // expected
        }
      }
    });

    tests.add("Object Value Missing Beginning Bracket", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {

        final JSONParser parser = factory.parser();
        try {
          parser.parse("\"name\":{\"first:\"sam\", \"last\":\"doe\" } }");
        } catch (IOException ex) {
          // expected
        }
      }
    });

    tests.add("Empty String", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {

        final JSONParser parser = factory.parser();
        try {
          parser.parse("");
        } catch (IOException ex) {
          // expected
        }
      }
    });

    tests.add("Object Value Missing Comma", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {

        final JSONParser parser = factory.parser();
        try {
          parser.parse("{ \"name\":{\"first\":\"sam\" \"last\":\"doe\" } }");
        } catch (IOException ex) {
          // expected
        }
      }
    });

    tests.add("Object Value Extra Whitespace", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {

        final JSONParser parser = factory.parser();
        final JSON obj = parser.parse("{ \"name\":{\"first\":\"sam\", \"last\":\"doe\" } }   \t\n  ");

        final JSON nameObj = obj.getObject("name");

        Asserts.isNotNull(nameObj);
        Asserts.isEqual("sam", nameObj.getString("first"));
        Asserts.isEqual("doe", nameObj.getString("last"));
      }
    });

    tests.add("Object Value Replaced", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {

        final JSONParser parser = factory.parser();
        final JSON obj = parser.parse("{ \"name\":{\"first\":\"sam\", \"last\":\"doe\" }, \"name\":\"sam\" }   \t\n  ");

        final String nameStr = obj.getString("name");

        Asserts.isNotNull(nameStr);
        Asserts.isEqual("sam", nameStr);
      }
    });


    tests.add("Object With Empty String Key", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {

        final JSONParser parser = factory.parser();
        final JSON obj = parser.parse("{ \"name\":{\"\":\"sam\", \"last\":\"doe\" } }");

        final JSON nameObj = obj.getObject("name");

        Asserts.isNotNull(nameObj);
        Asserts.isEqual("sam", nameObj.getString(""));
        Asserts.isEqual("doe", nameObj.getString("last"));
      }
    });

    tests.add("Object With Empty String Value", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {

        final JSONParser parser = factory.parser();
        final JSON obj = parser.parse("{ \"name\":{\"first\":\"\", \"last\":\"doe\" } }");

        final JSON nameObj = obj.getObject("name");

        Asserts.isNotNull(nameObj);
        Asserts.isEqual("", nameObj.getString("first"));
        Asserts.isEqual("doe", nameObj.getString("last"));
      }
    });


    tests.add("Object Replace Value", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {

        JSONParser parser = factory.parser();
        JSON obj = parser.parse("{ \"name\":{\"first\":\"sam\", \"first\":\"doe\" } }");

        final JSON nameObj = obj.getObject("name");

        Asserts.isNotNull(nameObj);
        Asserts.isEqual("doe", nameObj.getString("first"));

        parser = factory.parser();
        obj = parser.parse("{ \"name\":{\"first\":\"sam\", \"last\":\"doe\" }, \"name\":\"sam\" }   \t\n  ");

        final String nameStr = obj.getString("name");

        Asserts.isNotNull(nameStr);
        Asserts.isEqual("sam", nameStr);

      }
    });


    tests.add("Object Value Escape Char", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {

        final JSONParser parser = factory.parser();
        final JSON obj = parser.parse("{ \"name\":{\"first\":\"\\\"sam\\\"\", \"last\":\"\\\\doe\\t\\n\" } }");

        final JSON nameObj = obj.getObject("name");

        Asserts.isNotNull(nameObj);
        Asserts.isEqual("\"sam\"", nameObj.getString("first"));
        Asserts.isEqual("\\doe\t\n", nameObj.getString("last"));
      }
    });

    tests.run(new JSONFactory(){
      @Override
      public JSONParser parser() {
        return new MyJSONParser();
      }

      @Override
      public JSON object() {
        return new MyJSON();
      }
    });
  }
}
