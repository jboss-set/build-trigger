package io.xstefank.model.json;

public class Product {

    public String name;
    public String version;


    @Override
    public String toString() {
        return "Product{" +
            "name='" + name + '\'' +
            ", version='" + version + '\'' +
            '}';
    }
}
