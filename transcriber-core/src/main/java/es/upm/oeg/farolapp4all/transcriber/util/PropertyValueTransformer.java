package es.upm.oeg.farolapp4all.transcriber.util;

import es.upm.oeg.farolapp4all.transcriber.model.PropertyValue;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ext.com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static es.upm.oeg.farolapp4all.transcriber.util.Vocab.*;

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
public class PropertyValueTransformer {

    private static final Logger logger = LoggerFactory.getLogger(PropertyValueTransformer.class);

    private static final String ALTURA_PREFIX = "http://vocab.linkeddata.es/datosabiertos/kos/urbanismo-infraestructuras/alumbrado-publico/altura/";

    private static final String LAMPARA_PREFIX = "http://vocab.linkeddata.es/datosabiertos/kos/urbanismo-infraestructuras/alumbrado-publico/tipo-lampara/";

    private static final String LUZ_PREFIX = "http://vocab.linkeddata.es/datosabiertos/kos/urbanismo-infraestructuras/alumbrado-publico/tipo-luz/";

    private final static Map<String, String> PROP_ATTR_MAP = ImmutableMap.<String, String>builder()
            .put(A_COLOR, P_COLOR)
            .put(A_COVERED, P_PROTECCION)
            .put(A_WATTAGE, P_POTENCIA)
            .put(A_POLLUTION, P_CONTAMINACION)
            .put(A_HEIGHT, P_ALTURA)
            .put(A_HEADING, P_HEADING)
            .put(A_PITCH, P_PITCH)
            .put(A_LAMP, P_TIPO_DE_LAMPARA)
            .put(A_LIGHT, P_TIPO_DE_LUZ)
            .put(A_STATUS, P_ESTADO)
            .build();

    private final static Map<String, String> HEIGHT_MAP = ImmutableMap.<String, String>builder()
            .put("low", ALTURA_PREFIX + "Baja")
            .put("medium", ALTURA_PREFIX + "Media")
            .put("high", ALTURA_PREFIX + "Alta")
            .build();

    private final static Map<String, String> LAMP_MAP = ImmutableMap.<String, String>builder()
            .put("VSAP", LAMPARA_PREFIX + "VSAP")
            .put("VMCC", LAMPARA_PREFIX + "VMCC")
            .put("VMAP", LAMPARA_PREFIX + "VMAP")
            .put("PAR", LAMPARA_PREFIX + "PAR")
            .put("MC", LAMPARA_PREFIX + "MC")
            .put("LED", LAMPARA_PREFIX + "LED")
            .put("I", LAMPARA_PREFIX + "I")
            .put("H", LAMPARA_PREFIX+ "H")
            .put("VSBP", LAMPARA_PREFIX + "VSBP")
            .put("FCBC", LAMPARA_PREFIX + "FCBC")
            .put("HM", LAMPARA_PREFIX+ "HM")
            .put("F", LAMPARA_PREFIX + "F")
            .build();

    private final static Map<String, String> LIGHT_MAP = ImmutableMap.<String, String>builder()
            .put("P", LUZ_PREFIX + "P")
            .put("F", LUZ_PREFIX + "F")
            .put("E", LUZ_PREFIX + "E")
            .put("AA", LUZ_PREFIX + "AA")
            .put("AC", LUZ_PREFIX + "AC")
            .put("ER", LUZ_PREFIX + "ER")
            .put("O", LUZ_PREFIX + "O")
            .build();


    public static PropertyValue transform(String key, String value) {

        logger.debug("Transforming the key value pair - '{}':'{}'", key, value);

        String property = PROP_ATTR_MAP.get(key);
        if (property == null) {
            logger.error("Unknown attribute '{}'.", key);
            throw new IllegalArgumentException("Unknown attribute - " + key);
        }

        switch (property) {
            case P_COLOR:
            case P_ESTADO:
                return new PropertyValue(property, value, true);
            case P_PROTECCION:
                return new PropertyValue(property, value, true, XSDDatatype.XSDboolean);
            case P_POTENCIA:
            case P_HEADING:
            case P_PITCH:
                return new PropertyValue(property, value, true, XSDDatatype.XSDdecimal);
            case P_CONTAMINACION:
                return new PropertyValue(property, value, true);
            case P_ALTURA:
                String altura = HEIGHT_MAP.get(value);
                if (altura == null) {
                    logger.error("Unknown property value for height - '{}' ", value);
                    throw new IllegalArgumentException("Unknown property value for height:" + value);
                }
                return new PropertyValue(property, altura, false);
            case P_TIPO_DE_LAMPARA:
                String tipoDeLampara = LAMP_MAP.get(value);
                if (tipoDeLampara == null) {
                    logger.error("Unknown property value for lamp - '{}' ", value);
                    throw new IllegalArgumentException("Unknown property value for lamp:" + value);
                }
                return new PropertyValue(property, tipoDeLampara, false);
            case P_TIPO_DE_LUZ:
                String tipoDeLuz = LAMP_MAP.get(value);
                if (tipoDeLuz == null) {
                    logger.error("Unknown property value for light - '{}' ", value);
                    throw new IllegalArgumentException("Unknown property value for light:" + value);
                }
                return new PropertyValue(property, tipoDeLuz, false);
            default:
                throw new IllegalArgumentException("Unknown attribute (in Map) - " + key);
        }
    }
}
