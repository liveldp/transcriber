package es.upm.oeg.farolapp4all.transcriber.model;

import org.apache.jena.datatypes.RDFDatatype;

/**
 * Copyright 2014-2016 Ontology Engineering Group, Universidad Politécnica de Madrid, Spain
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Nandana Mihindukulasooriya
 * @since 1.0.0
 */
public class PropertyValue {

    String property;

    String value;

    boolean literal;

    RDFDatatype datatype;

    public PropertyValue(String property, String value, boolean literal) {
        this.property = property;
        this.value = value;
        this.literal = literal;
    }

    public PropertyValue(String property, String value, boolean literal, RDFDatatype datatype) {
        this.property = property;
        this.value = value;
        this.literal = literal;
        this.datatype = datatype;
    }

    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }

    public boolean isLiteral() {
        return literal;
    }

    public RDFDatatype getDatatype() {
        return datatype;
    }
}
