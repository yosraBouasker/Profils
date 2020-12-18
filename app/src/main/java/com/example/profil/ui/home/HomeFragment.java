package com.example.profil.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.profil.JSONParser;
import com.example.profil.Profil;
import com.example.profil.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    Button btntelecharger;
    ListView lv;
    ArrayList<Profil> data = new ArrayList<Profil>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //root: frag home

        //recuperation home
        lv = root.findViewById(R.id.lv_home);
        btntelecharger = root.findViewById(R.id.btntelecharger_home);

        btntelecharger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //lancer un thread de telechargement
                /**
                 * extends Thread + implements Runnable ==> Handler
                 * 2eme methode: AsyncTask
                 */
                Telechargement t = new Telechargement(HomeFragment.this.getActivity());
                t.execute(); //on peut lui passer des params
            }
        });
        return root;
    }

    class Telechargement extends AsyncTask {


        Context con;
        AlertDialog alert;

        public Telechargement(Context con) {
            this.con = con;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //UIT
//            AlertDialog.Builder dialog = new AlertDialog.Builder(HomeFragment.this.getActivity());
            AlertDialog.Builder dialog = new AlertDialog.Builder(con);
            dialog.setTitle("Téléchargement");
            dialog.setMessage("Veuillez patientez...");
            dialog.show();
            alert = dialog.create();
            alert.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            // ==> elle correspond à la méthode RUN / 2eme processus

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /* btntelecharger.setText("50% terminé.."); ==> il faut la lancer dans l'UIT
            //elle va générer une interrupted exception, plantage */
            publishProgress(1); //appel imlicite de onProgreeUpdate

            String ip = "192.168.1.16"; //Ipv4
            //nom de votre site en cas d'hebergement
            //10.0.2.2 : si on travaille avec AVD
            String url = "http://"+ ip + "/servicephp/get_all_user.php";

            JSONObject response = JSONParser.makeRequest(url); //executer le fichier php
                                                               //Toast ici ne marche pas
            try {
                int s = response.getInt("success");

                if (s== 0){
                    String msg = response.getString("message");
                }
                else {
                    JSONArray tab = response.getJSONArray("profil");
                    for (int i=0; i<tab.length(); i++){
                        JSONObject ligne = tab.getJSONObject(i);
                        String n = ligne.getString("nom");
                        String p = ligne.getString("prenom");
                        String ps = ligne.getString("pseudo");
                        data.add(new Profil(n, p, ps));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /* btntelecharger.setBackgroundColor(Color.RED);  ==> il faut la lancer dans l'UIT
            //erreur */
            publishProgress(2);

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            //UIT
            if (values[0] == (Object)1){
                btntelecharger.setText("50% terminé..");
            }
            else
                btntelecharger.setBackgroundColor(Color.RED);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //UIT: UIThread
            alert.dismiss(); //il faut la faire dans onPostExecute pour qu'elle ne persiste pas
            ArrayAdapter ad = new ArrayAdapter(con,
                    android.R.layout.simple_list_item_1,
                    data);
            lv.setAdapter(ad);
        }
    }
}