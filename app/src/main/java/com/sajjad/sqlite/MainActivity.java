package com.sajjad.sqlite;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.sajjad.sqlite.SQLitePackage.PersonModel;
import com.sajjad.sqlite.SQLitePackage.SQLiteHelper;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int personId, position = 0, personAge;
    String personName;
    byte[] imageBytes;
    Toolbar mainToolbar;
    EditText personNameEdit, personAgeEdit, searchEdit;
    ImageView personImage;
    //
    List<PersonModel> personModels;
    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        sqLiteHelper = new SQLiteHelper(this);
    }

    private void initViews() {
        mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        personNameEdit = findViewById(R.id.personNameText);
        personAgeEdit = findViewById(R.id.personAgeText);
        searchEdit = findViewById(R.id.searchPersonEdit);
        personImage = findViewById(R.id.personImageView);
        searchEdit.addTextChangedListener(searchListener);
    }

    private byte[] imageToBytes(ImageView personImage) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) personImage.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }

    private Bitmap bytesToImage(byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    private void insertPerson() {
        fillData();
        imageBytes = imageToBytes(personImage);
        sqLiteHelper.insertPerson(personName, personAge, imageBytes);
        personModels = sqLiteHelper.getAll();
        showPersonData(position);
    }

    private void updatePerson() {
        fillData();
        imageBytes = imageToBytes(personImage);
        sqLiteHelper.updatePerson(personId, personName, personAge, imageBytes);
        personModels=sqLiteHelper.getAll();
        showPersonData(position);
    }

    private void removePerson() {
        sqLiteHelper.removePerson(personId);
    }

    private void fillData() {
        personName = personNameEdit.getText().toString();
        personAge = Integer.valueOf(personAgeEdit.getText().toString());
        imageBytes = imageToBytes(personImage);
    }

    private void loadImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1010);
    }

    private void loadPersonData() {
        personModels = sqLiteHelper.getAll();
        position = 0;
        showPersonData(position);
    }

    private void nextPerson(){
        if (position!=personModels.size()-1){
            position++;
            //personId = personModels.get(position).getId();
            showPersonData(position);
        }
    }

    private void previousPerson() {
        if (position != 0) {
            position--;
            //personId = personModels.get(position).getId();
            showPersonData(position);
        }
    }

    private void searchPerson(String searchText){
        for (PersonModel personModel:personModels){
            if (personModel.getPersonName().toLowerCase().trim().contains(searchText.toLowerCase().trim())){
                personId=personModel.getId();
                personNameEdit.setText(personModel.getPersonName());
                personAgeEdit.setText(String.valueOf( personModel.getAge()));
                personImage.setImageBitmap(bytesToImage(personModel.getImageBytes()));
            }
        }
    }

    private void showPersonData(int position) {
        PersonModel personModel=personModels.get(position);
        personId = personModel.getId();
        personNameEdit.setText(personModel.getPersonName());
        personAgeEdit.setText(String.valueOf( personModel.getAge()));
        personImage.setImageBitmap(bytesToImage(personModel.getImageBytes()));
    }

    TextWatcher searchListener=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            searchPerson(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1010 && resultCode == RESULT_OK) {
            if (data != null) {
                personImage.setImageURI(data.getData());
            }
        }
    }

    public void loadDataClick(View view) {
        loadPersonData();
    }

    public void insertPersonClick(View view) {
        insertPerson();
    }

    public void updatePersonClick(View view) {
        updatePerson();
    }

    public void removePersonClick(View view) {
        removePerson();
    }

    public void loadImageFromGalleryClick(View view) {
        loadImageFromGallery();
    }

    public void nextClick(View view) {
        nextPerson();
    }

    public void previousClick(View view) {
        previousPerson();
    }
}