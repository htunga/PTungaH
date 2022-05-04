package com.example.ptunga;

public class Item {
    int eImage;
    String eName,eColor,eDescription;
    int eSize;

    public Item(int eImage, String eName, String eColor, String eDescription, int eSize) {
        this.eImage = eImage;
        this.eName = eName;
        this.eColor = eColor;
        this.eDescription = eDescription;
        this.eSize = eSize;
    }

    public int geteImage() {
        return eImage;
    }

    public void seteImage(int eImage) {
        this.eImage = eImage;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String geteColor() {
        return eColor;
    }

    public void seteColor(String eColor) {
        this.eColor = eColor;
    }

    public String geteDescription() {
        return eDescription;
    }

    public void seteDescription(String eDescription) {
        this.eDescription = eDescription;
    }

    public int geteSize() {
        return eSize;
    }

    public void seteSize(int eSize) {
        this.eSize = eSize;
    }
}
