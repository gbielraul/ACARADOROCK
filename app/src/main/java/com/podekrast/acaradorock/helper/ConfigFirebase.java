package com.podekrast.acaradorock.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfigFirebase {

    private static FirebaseAuth auth;
    private static DatabaseReference dbReference;
    private static StorageReference storage;

    public static DatabaseReference getDbReference() {
        if (dbReference == null)
            dbReference = FirebaseDatabase.getInstance().getReference();
        return dbReference;
    }

    public static StorageReference getStorage() {
        if (storage == null)
            storage = FirebaseStorage.getInstance().getReference();
        return storage;
    }

    public static FirebaseAuth getFirebaseAuth() {
        if (auth == null)
            auth = FirebaseAuth.getInstance();
        return auth;
    }
}
