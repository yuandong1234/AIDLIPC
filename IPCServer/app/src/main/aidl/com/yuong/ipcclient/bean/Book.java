package com.yuong.ipcclient.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yuandong on 2018/8/1.
 */

public class Book implements Parcelable{
   public String name;
   public int price;


    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Book() {
    }

    protected Book(Parcel in) {
        name = in.readString();
        price = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(price);
    }

    public void readFromParcel(Parcel dest){
        name=dest.readString();
        price=dest.readInt();
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
