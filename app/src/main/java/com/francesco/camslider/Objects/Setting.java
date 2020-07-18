package com.francesco.camslider.Objects;

public class Setting {

    //6
    String distance;
    String distance_um;
    String rotation;
    String rotation_um;
    String tilting;
    String tilting_um;

    //12
    String slide_left;
    String slide_left_value;
    String slide_right;
    String slide_right_value;

    String tilt_down;
    String tilt_down_value;
    String tilt_up;
    String tilt_up_value;

    String rotate_CCW;
    String rotate_CCW_value;
    String rotate_CW;
    String rotate_CW_value;



    //4
    String home;
    String home_value;
    String end;
    String end_value;

    //2
    String top_value;
    String joystick_frequency_value;


    public Setting() { }

    public Setting(String distance, String distance_um, String rotation, String rotation_um, String tilting, String tilting_um, String slide_left, String slide_left_value, String slide_right, String slide_right_value, String tilt_down, String tilt_down_value, String tilt_up, String tilt_up_value, String rotate_CCW, String rotate_CCW_value, String rotate_CW, String rotate_CW_value, String home, String home_value, String end, String end_value, String top_value, String joystick_frequency_value) {
        this.distance = distance;
        this.distance_um = distance_um;
        this.rotation = rotation;
        this.rotation_um = rotation_um;
        this.tilting = tilting;
        this.tilting_um = tilting_um;
        this.slide_left = slide_left;
        this.slide_left_value = slide_left_value;
        this.slide_right = slide_right;
        this.slide_right_value = slide_right_value;
        this.tilt_down = tilt_down;
        this.tilt_down_value = tilt_down_value;
        this.tilt_up = tilt_up;
        this.tilt_up_value = tilt_up_value;
        this.rotate_CCW = rotate_CCW;
        this.rotate_CCW_value = rotate_CCW_value;
        this.rotate_CW = rotate_CW;
        this.rotate_CW_value = rotate_CW_value;
        this.home = home;
        this.home_value = home_value;
        this.end = end;
        this.end_value = end_value;
        this.top_value = top_value;
        this.joystick_frequency_value = joystick_frequency_value;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistance_um() {
        return distance_um;
    }

    public void setDistance_um(String distance_um) {
        this.distance_um = distance_um;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    public String getRotation_um() {
        return rotation_um;
    }

    public void setRotation_um(String rotation_um) {
        this.rotation_um = rotation_um;
    }

    public String getTilting() {
        return tilting;
    }

    public void setTilting(String tilting) {
        this.tilting = tilting;
    }

    public String getTilting_um() {
        return tilting_um;
    }

    public void setTilting_um(String tilting_um) {
        this.tilting_um = tilting_um;
    }

    public String getSlide_left() {
        return slide_left;
    }

    public void setSlide_left(String slide_left) {
        this.slide_left = slide_left;
    }

    public String getSlide_left_value() {
        return slide_left_value;
    }

    public void setSlide_left_value(String slide_left_value) {
        this.slide_left_value = slide_left_value;
    }

    public String getSlide_right() {
        return slide_right;
    }

    public void setSlide_right(String slide_right) {
        this.slide_right = slide_right;
    }

    public String getSlide_right_value() {
        return slide_right_value;
    }

    public void setSlide_right_value(String slide_right_value) {
        this.slide_right_value = slide_right_value;
    }

    public String getRotate_CCW() {
        return rotate_CCW;
    }

    public void setRotate_CCW(String rotate_CCW) {
        this.rotate_CCW = rotate_CCW;
    }

    public String getRotate_CCW_value() {
        return rotate_CCW_value;
    }

    public void setRotate_CCW_value(String rotate_CCW_value) {
        this.rotate_CCW_value = rotate_CCW_value;
    }

    public String getRotate_CW() {
        return rotate_CW;
    }

    public void setRotate_CW(String rotate_CW) {
        this.rotate_CW = rotate_CW;
    }

    public String getRotate_CW_value() {
        return rotate_CW_value;
    }

    public void setRotate_CW_value(String rotate_CW_value) {
        this.rotate_CW_value = rotate_CW_value;
    }

    public String getTilt_down() {
        return tilt_down;
    }

    public void setTilt_down(String tilt_down) {
        this.tilt_down = tilt_down;
    }

    public String getTilt_down_value() {
        return tilt_down_value;
    }

    public void setTilt_down_value(String tilt_down_value) {
        this.tilt_down_value = tilt_down_value;
    }

    public String getTilt_up() {
        return tilt_up;
    }

    public void setTilt_up(String tilt_up) {
        this.tilt_up = tilt_up;
    }

    public String getTilt_up_value() {
        return tilt_up_value;
    }

    public void setTilt_up_value(String tilt_up_value) {
        this.tilt_up_value = tilt_up_value;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getHome_value() {
        return home_value;
    }

    public void setHome_value(String home_value) {
        this.home_value = home_value;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getEnd_value() {
        return end_value;
    }

    public void setEnd_value(String end_value) {
        this.end_value = end_value;
    }

    public String getJoystick_frequency_value() {
        return joystick_frequency_value;
    }

    public void setJoystick_frequency_value(String joystick_frequency_value) {
        this.joystick_frequency_value = joystick_frequency_value;
    }

    public String getTop_value() {
        return top_value;
    }

    public void setTop_value(String top_value) {
        this.top_value = top_value;
    }
}
