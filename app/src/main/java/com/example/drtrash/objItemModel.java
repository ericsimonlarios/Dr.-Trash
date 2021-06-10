package com.example.drtrash;


import java.util.Comparator;

public class objItemModel implements Comparable<objItemModel>{

    private String objTitle;
    private String objType;
    private String objClass;
    private String imgUrl;
    private String views;



    public objItemModel (String objTitle, String objType, String objClass, String imgUrl, String views){

        this.objTitle=objTitle;
        this.objType=objType;
        this.objClass=objClass;
        this.imgUrl=imgUrl;
        this.views=views;
    }


    public void setViews(String views){

        this.views=views;

    }

    public String getViews(){

       return views;

    }

    public String getObjTitle() {
        return objTitle;
    }

    public void setObjTitle(String objTitle) {
        this.objTitle = objTitle;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getObjClass() {
        return objClass;
    }

    public void setObjClass(String objClass) {
        this.objClass = objClass;
    }

    public void setImgUrl(String imgUrl){
        this.imgUrl=imgUrl;
    }

    public String getImgUrl(){
        return imgUrl;
    }


    @Override
    public String toString() {
        return "objItemModel{" +
                "objTitle='" + objTitle + '\'' +
                ", objType='" + objType + '\'' +
                ", objClass='" + objClass + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                /*", views=" + views +*/
                '}';
    }

    @Override
    public int compareTo(objItemModel o) {
        int first = Integer.parseInt(this.views);
        int second = Integer.parseInt(o.getViews());
        return second-first;
    }

    public static Comparator<objItemModel> sort = new Comparator<objItemModel>() {
        @Override
        public int compare(objItemModel e1, objItemModel e2) {
            return e2.getViews().compareTo(e1.getViews());
        }
    };
}
