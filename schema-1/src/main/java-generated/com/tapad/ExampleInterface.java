/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.tapad;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public interface ExampleInterface {
  public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse("{\"protocol\":\"ExampleInterface\",\"namespace\":\"com.tapad\",\"types\":[{\"type\":\"enum\",\"name\":\"ExampleEnum\",\"symbols\":[\"FOO\",\"BAR\"]},{\"type\":\"record\",\"name\":\"ExampleNesting\",\"fields\":[{\"name\":\"counts\",\"type\":{\"type\":\"array\",\"items\":\"int\"}},{\"name\":\"attributes\",\"type\":[\"null\",{\"type\":\"map\",\"values\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"avro.java.string\":\"String\"}],\"default\":null}]},{\"type\":\"record\",\"name\":\"Example\",\"fields\":[{\"name\":\"id\",\"type\":\"long\"},{\"name\":\"my_enum\",\"type\":\"ExampleEnum\"},{\"name\":\"my_optional_member\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"default\":null},{\"name\":\"my_nested_member\",\"type\":[\"null\",\"ExampleNesting\"],\"default\":null}]}],\"messages\":{}}");

  @SuppressWarnings("all")
  public interface Callback extends ExampleInterface {
    public static final org.apache.avro.Protocol PROTOCOL = com.tapad.ExampleInterface.PROTOCOL;
  }
}