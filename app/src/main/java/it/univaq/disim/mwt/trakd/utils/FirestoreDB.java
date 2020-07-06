package it.univaq.disim.mwt.trakd.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirestoreDB {

    private static FirestoreDB instance = null;
    private FirebaseFirestore db;

    public synchronized static FirestoreDB getInstance(){
        return (instance == null) ? instance = new FirestoreDB() : instance;
    }

    private FirestoreDB(){
        this.db = FirebaseFirestore.getInstance();
    }

    public Task<DocumentSnapshot> getData(String userEmail){
        return db.collection("data")
                .document(userEmail)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(FirestoreDB.class.getName(), e);
                    }
                });
    }

    public void putData(String userEmail, String jsonData){
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("data", jsonData);

        db.collection("data")
                .document(userEmail)
                .set(dataMap)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(FirestoreDB.class.getName(), e);
                    }
                });
    }
}
