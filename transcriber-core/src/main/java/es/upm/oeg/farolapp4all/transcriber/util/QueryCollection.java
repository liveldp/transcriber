package es.upm.oeg.farolapp4all.transcriber.util;

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
public class QueryCollection {

    public static String updateQuery = "prefix ap: <http://vocab.linkeddata.es/datosabiertos/def/urbanismo-infraestructuras/alumbrado-publico#>\n" +
            "prefix dc: <http://purl.org/dc/terms/>\n" +
            "\n" +
            "DELETE { GRAPH <http://farolas.linkeddata.es/resource> { ?uri ?prop ?oldValue; ap:time ?oldTime}  }\n" +
            "INSERT { GRAPH <http://farolas.linkeddata.es/resource> { ?uri ?prop ?newValue; ap:time ?timeNew} }\n" +
            "where { ?uri a ap:PuntoDeAlumbrado . OPTIONAL { ?uri ?prop ?oldValue . } OPTIONAL { ?uri ap:time ?oldTime . }  }";

    public static String insertGPSQuery = "prefix ap: <http://vocab.linkeddata.es/datosabiertos/def/urbanismo-infraestructuras/alumbrado-publico#>\n" +
            "prefix dc: <http://purl.org/dc/terms/>\n" +
            "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
            "INSERT { GRAPH <http://farolas.linkeddata.es/resource> { ?uri a ap:PuntoDeAlumbrado; geo:long ?long; geo:lat ?lat; " +
            "ap:time ?timeNew; ap:id ?id } } where { ?s ?p ?o }" ;

    public static String minPropertiesQuery = "prefix ap: <http://vocab.linkeddata.es/datosabiertos/def/urbanismo-infraestructuras/alumbrado-publico#>\n" +
            "prefix dc: <http://purl.org/dc/terms/>\n" +
            "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
            "select ?id ?lat ?long ?heading ?pitch where { GRAPH <http://farolas.linkeddata.es/resource> { ?uri a ap:PuntoDeAlumbrado; ap:id ?id; geo:long ?long; geo:lat ?lat; ap:heading ?heading; ap:pitch ?pitch }} " ;
}
