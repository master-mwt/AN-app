package it.univaq.disim.mwt.android_native_app.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public Map<String, Object> getData(String userEmail){
        final Map<String, Object>[] map = new Map[]{ null };

        db.collection("data")
                .document(userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot result = task.getResult();
                            map[0] = result != null ? result.getData() : null;
                        } else {
                            Log.w(FirestoreDB.class.getName(), task.getException());
                        }
                    }
                });
        return map[0];
    }

    public void putData(String userEmail, String jsonData){
        db.collection("data")
                .document(userEmail)
                .set(jsonData)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(FirestoreDB.class.getName(), e);
                    }
                });
    }
}
