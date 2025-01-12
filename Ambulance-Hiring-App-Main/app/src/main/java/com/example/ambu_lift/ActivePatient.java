package com.example.ambu_lift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ActivePatient extends AppCompatActivity {
    ListView activepai;

    ArrayList<String> myArraylist=new ArrayList<>();
    DatabaseReference reference;
    String uid;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_patient);

        String drivername=getIntent().getStringExtra("drivername").toString();
        String dmobile=getIntent().getStringExtra("dmobile").toString();
        ArrayAdapter<String> myArrayAdapter= new ArrayAdapter<String>(ActivePatient.this, android.R.layout.simple_expandable_list_item_1,myArraylist);

        activepai=findViewById(R.id.emerpai);
        activepai.setAdapter(myArrayAdapter);

        user= FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();


        reference = FirebaseDatabase.getInstance().getReference("Patients");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    String pname = snapshot.child("pname").getValue(String.class);

//                    String age = snapshot.child("pAge").getValue(String.class);
                    String date=snapshot.child("Booking").child("Date").getValue(String.class);
                    String time=snapshot.child("Booking").child("Time").getValue(String.class);
                    String pick=snapshot.child("Booking").child("Pickup").getValue(String.class);
                    String drop=snapshot.child("Booking").child("DropAt").getValue(String.class);
                    String pno=snapshot.child("pmbno").getValue(String.class);
                    String value="Mobile No: "+pno+"    Name: "+pname+"\nSchedule: "+date+", At "+time+"\nRoute: "
                            +pick+" to "+drop+"\n ____________________________________________________________________ ";
                    myArrayAdapter.add(value);

                    myArrayAdapter.notifyDataSetChanged();


                    activepai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String pai = parent.getItemAtPosition(position).toString();
                            String mno=pai.substring(11, 22);
                            Intent intent=new Intent(ActivePatient.this,Patientlog.class);
                            intent.putExtra("mbno",mno);
                            intent.putExtra("name",drivername);
                            intent.putExtra("mobile",dmobile);
                            startActivity(intent);

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}