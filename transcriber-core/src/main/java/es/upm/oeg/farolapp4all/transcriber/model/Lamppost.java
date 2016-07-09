package es.upm.oeg.farolapp4all.transcriber.model;

import com.google.gson.Gson;

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
public class Lamppost implements JSONMessage {

    private String uri;

    private String id;

    private double lattitude;

    private double longitude;

    private double heading;

    private double pitch;

    private static Gson gson = new Gson();

    public Lamppost(){

    }

    public Lamppost(String uri, String id, double lattitude, double longitude, double heading, double pitch) {
        this.uri = uri;
        this.id = id;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.heading = heading;
        this.pitch = pitch;
    }

    public String getUri() {
        return uri;
    }

    public double getLattitude() {
        return lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getHeading() {
        return heading;
    }

    public double getPitch() {
        return pitch;
    }

    public String getId() {
        return id;
    }

    public String toJSON() {
        return gson.toJson(this);
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static Lamppost fromJSON(String msg) {
        return (Lamppost) gson.fromJson(msg, Lamppost.class);
    }

    public String toString() {
        return String.format("Lamppost:\nUri:%s\nLattitude:%1$,.2f" +
                "\nLongitude:%1$,.2f\nHeading:%1$,.2f\nPitch:%1$,.2f",
                uri, lattitude, longitude, heading, pitch
        );
    }


}
