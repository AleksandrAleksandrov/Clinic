package com.aleksandr.aleksandrov.clinic;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import adapters.StringWithTag;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class Specializations {

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs1;

    URL url = null;
    HttpURLConnection urlConnection;
    JSONArray appspecializations = null;
    JSONObject jsonObj = null;
    List<StringWithTag> itemList = null;
    private String error = null;
    public Specializations() {
        try {
//            con = DriverManager.getConnection(Resources.URL, Resources.PROPERTIES);
//            stmt = con.createStatement();
            // byte[] b = tfFirstName.getText().getBytes();
            // String m = b.toString();
            // System.out.println(arg0);
//            String query = "insert into appspecialization (firstName, lastName) values ('" + tfFirstName.getText() + "', '" + tfLastName.getText() + "');";

//            stmt.executeUpdate(query);
            String query2 = "select id, name, description from appspecialization";

            rs1 = stmt.executeQuery(query2);
//            EmoStateLog.myMapAL.clear();
            itemList = new ArrayList<StringWithTag>();
            Log.d("st", "111111111111111111");
            while (rs1.next()) {
                itemList.add(new StringWithTag(rs1.getString("name"), Integer.toString(rs1.getInt("id"))));
//                EmoStateLog.myMapAL.add(new MyMap(rs1.getInt("id"), rs1.getString("firstName"), rs1.getString("lastName")));
            }
            for (StringWithTag r : itemList) {
                Log.d("st", r.getString());
            }
//            list.removeAll();
//            list.setListData(EmoStateLog.myMapAL.toArray());
//            list.updateUI();
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            try {

                con.close();
                stmt.close();
                rs1.close();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
//        try {
//            url = new URL(Resources.url_all_appspecialization);
//            urlConnection = (HttpURLConnection) url.openConnection();
//            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//            StringBuilder result = new StringBuilder();
//            String line;
//            while((line = reader.readLine()) != null) {
//                result.append(line);
//            }
//            jsonObj = new JSONObject(result.toString());
//
//            int success = jsonObj.getInt(Resources.TAG_SUCCESS);
//            if (success == 1) {
//                appspecializations = jsonObj.getJSONArray(Resources.TAG_APPSPECIALIZATIONS);
//                itemList = new ArrayList<StringWithTag>();
//                for (int i = 0; i < appspecializations.length(); i++) {
//                    JSONObject c = appspecializations.getJSONObject(i);
//
//                    String id = c.getString(Resources.TAG_APPSPECIALIZATION_ID);
//                    String name = c.getString(Resources.TAG_APPSPECIALIZATION_NAME);
//
//                    itemList.add(new StringWithTag(name, id));
//                }
//            }
//        } catch (MalformedURLException e) {
//            error = "Что-то пошло не так";
//            e.printStackTrace();
//        } catch (IOException e) {
//            error = "Не удалось подкулючится к серверу";
//            Log.d("myLogs", error);
//            e.printStackTrace();
//        } catch (JSONException e) {
//            error = "Что-то пошло не так";
//            e.printStackTrace();
//        } finally {
//            urlConnection.disconnect();
//        }
    }

    public List answer() {
        return itemList;
    }

    public String error() {
        return error;
    }
}