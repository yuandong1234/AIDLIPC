package com.yuong.ipcclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yuong.ipcclient.bean.Book;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    private TextView mContent;


    private boolean isBound = false;
    private BookManager mBookManager;
    private StringBuilder mBuilder = new StringBuilder();

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "service connected !");
            isBound = true;
            mBookManager = BookManager.Stub.asInterface(service);

            mBuilder.append(" service connected !").append("\n");
            mContent.setText(mBuilder.toString());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "service disconnected !");
            isBound = false;
            mBookManager = null;

            mBuilder.append(" service disconnected !").append("\n");
            mContent.setText(mBuilder.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mContent = findViewById(R.id.content);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                if (isBound) {
                    Toast.makeText(this, "service has already connected !", Toast.LENGTH_SHORT).show();
                    return;
                }
                mBuilder.append(" connect the service ....").append("\n");
                mContent.setText(mBuilder.toString());
                bindService();
                break;
            case R.id.button2:
                if (!isBound) {
                    Toast.makeText(this, "service is not  connected !", Toast.LENGTH_SHORT).show();
                    return;
                }

                mBuilder.append(" disconnect the service !").append("\n");
                mContent.setText(mBuilder.toString());
                unbindService(connection);
                isBound = false;
                break;
            case R.id.button3:
                addBook();
                break;
            case R.id.button4:
                getBooks();
                break;
        }
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setAction("com.yuong.aidl");
        intent.setPackage("com.yuong.ipcserver");
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
        }
    }

    private void addBook() {
        if (!isBound || mBookManager == null) {
            Toast.makeText(this, "service is not  connected !", Toast.LENGTH_SHORT).show();
            return;
        }
        Book book = new Book();
        book.setName("APP研发录In");
        book.setPrice(30);
        mBuilder.append(" add book : " + book.toString()).append("\n");
        mContent.setText(mBuilder.toString());
        try {
            mBookManager.addBook(book);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void getBooks() {
        if (!isBound || mBookManager == null) {
            Toast.makeText(this, "service is not  connected !", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            List<Book> books = mBookManager.getBooks();
            mBuilder.append(" get book : " + books.toString()).append("\n");
            mContent.setText(mBuilder.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
