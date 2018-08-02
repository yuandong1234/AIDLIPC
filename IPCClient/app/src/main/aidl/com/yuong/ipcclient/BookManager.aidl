// BookManager.aidl
package com.yuong.ipcclient;
import com.yuong.ipcclient.bean.Book;

// Declare any non-default types here with import statements

interface BookManager {

    List<Book> getBooks();
    void addBook(inout Book book);
}
