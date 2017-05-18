package com.tigon.biniam.android.command_deligation_demo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static xdroid.toaster.Toaster.toast;
import static xdroid.toaster.Toaster.toastLong;


public class MainActivity extends AppCompatActivity {
    Map<String, String> fidelMap = new HashMap<String, String>();
    static final int PICK_CONTACT = 1;
    public EditText input;
    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    public String contactName;
    public String contactNumber;
    public String amharicName;
    public String messageContent;
    public Boolean isCall = false;
    public Boolean isMail = false;
    public String subject;
    public String body;
    public String receiverEmail;
    public String senderEmail = "se.biniam.anteneh@gmail.com";
    public String newContactPhone = "";
    public GMailSender sender;
    public Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.inputText);

        loadFidelMap();

        context = this;

    }

    public void executeCommand(View view) {
        //Toast.makeText(this,fidelMap.get("በ"),Toast.LENGTH_SHORT).show();
        //String str = input.getText().toString().trim();
        String str = input.getText().toString().trim().replaceAll("[.]", "");

        ArrayList command = new ArrayList();
        //command.add(str.charAt(0)+"");
        String arg = "";
        for(int i=0; i<str.length(); i++){
            if(str.charAt(i) == ' '){
                if(arg != ""){
                    Log.d(arg,"test");

                    command.add(arg.trim());
                }
                arg = "";
            }
            arg = arg + str.charAt(i)+"";
            if(i == str.length()-1){
                if(arg !=""){

                    command.add(arg.trim());
                }
            }
        }
        //check if the length of the command arraylist
        if(command.size() == 1){
            String com = command.get(0).toString();
            //time
            if(com.compareToIgnoreCase("ሠዓት") ==0 || com.compareToIgnoreCase("ሰዓት")==0 || com.compareToIgnoreCase("ሠአት")==0 || com.compareToIgnoreCase("ሰአት")==0){
                tellTime();
            }
            //cal
            else if(com.contains("ደዉ") || com.contains("ደው") || com.contains("ደዉል") || com.contains("ደውል") || com.contains("ደዉል")|| com.contains("ደውይ") || com.contains("ደዉይ") || com.contains("ደውሉ") || com.contains("ደዉሉ")){
                isCall = true;
                isMail = false;
                getCallerName();
            }
            //message
            else if(com.contains("መልክት") || com.contains("መልእክት") || com.contains("መልዓክት") || com.contains("ሜሴጅ") || com.contains("ሜ\u1224ጅ")){
                isCall = false;
                isMail = false;
                getCallerName();
            }
            //new contact
            else if(com.contains("ስልክ")){
                setContentView(R.layout.new_contact_1);
            }
            else{
                Toast.makeText(this,"Command not recognized! " + com,Toast.LENGTH_SHORT).show();
            }

        }
        else if(command.size() == 2){
            String com1 = command.get(0).toString();
            String com2 = command.get(1).toString();

            //time
            Boolean containsTime = com1.compareToIgnoreCase("ሠዓት") == 0 || com1.compareToIgnoreCase("ሰዓት") ==0 || com1.compareToIgnoreCase("ሠአት") ==0 || com1.compareToIgnoreCase("ሰአት")==0;
            Boolean containsTime2 = com2.compareToIgnoreCase("ሠዓት") == 0 || com2.compareToIgnoreCase("ሰዓት") ==0 || com2.compareToIgnoreCase("ሠአት") ==0 || com2.compareToIgnoreCase("ሰአት")==0;
            Boolean containsCallPhrase1 = com1.contains("ደዉ") || com1.contains("ደው") || com1.contains("ደዉል") || com1.contains("ደውል") || com1.contains("ደዉል")|| com1.contains("ደውይ") || com1.contains("ደዉይ") || com1.contains("ደውሉ") || com1.contains("ደዉሉ");
            Boolean containsCallPhrase2 = com2.contains("ደዉ") || com2.contains("ደው") || com2.contains("ደዉል") || com2.contains("ደውል")|| com2.contains("ደዉል")|| com2.contains("ደውይ") || com2.contains("ደዉይ") || com2.contains("ደውሉ") || com2.contains("ደዉሉ");
            Boolean containsMessagePhrase1 = com1.contains("መልክት") || com1.contains("መልእክት") || com1.contains("መልዓክት") || com1.contains("ሜሴጅ") || com1.contains("ሜ\u1224ጅ");
            Boolean containsMessagePhrase2 = com2.contains("መልክት") || com2.contains("መልእክት") || com2.contains("መልዓክት") || com2.contains("ሜሴጅ") || com2.contains("ሜ\u1224ጅ");
            Boolean containsMessageSufix1 = com1.contains("ላክ") || com1.contains("ላኪ") || com1.contains("ላኩ");
            Boolean containsMessageSufix2 = com2.contains("ላክ") || com2.contains("ላኪ") || com2.contains("ላኩ");
            Boolean containsEmailPharase1 = com1.compareToIgnoreCase("ኢሜል") == 0;
            Boolean containsEmailPharase2 = com2.compareToIgnoreCase("ኢሜል") == 0;


            //time
            if(containsTime){
                tellTime();
            }
            //time
            else if((com1.compareToIgnoreCase("ስንት") ==0 || com1.compareToIgnoreCase("ንገሪኝ") ==0 || com1.compareToIgnoreCase("ንገረኝ") ==0) && (containsTime2)){
                tellTime();
            }
            //call
            else if(containsCallPhrase1){
                if(com2.length()>1){
                    amharicName = com2.substring(1);
                }
                String phone = getContact();
                if(phone != "Unsaved"){
                    makeACall(phone);
                }
                else{
                    Toast.makeText(this,"contact not found" ,Toast.LENGTH_SHORT).show();
                }

            }
            //call
            else if(containsCallPhrase2 ){
                if(com1.length()>1){
                    amharicName = com1.substring(1);
                }
                String phone = getContact();
                if(phone != "Unsaved"){
                    makeACall(phone);
                }
                else{
                    Toast.makeText(this,"contact not found" ,Toast.LENGTH_SHORT).show();
                }
            }
            //message
            else if((containsMessagePhrase1 && containsMessageSufix2) || (containsMessagePhrase2 && containsMessageSufix1)){
                isCall = false;
                isMail = false;
                getCallerName();
            }
            //email1
            else if((containsEmailPharase1 && containsMessageSufix2) || (containsEmailPharase2 && containsMessageSufix1)){
                isCall = false;
                isMail = true;
                getEmailContent();
            }
            //alarm
            else if(command.contains("አላርም") || command.contains("ማንቂያ")){
                setContentView(R.layout.alarm_layout_1);
            }
            //new contact
            else if(com2.contains("ስልክ") && com1.contains("አዲስ") ){
                setContentView(R.layout.new_contact_1);
            }
            else if(com2.contains("መዝግ") && com1.contains("ስልክ") ){
                setContentView(R.layout.new_contact_1);
            }

            else{
                Toast.makeText(this,"len: 2, Command not recognized:" ,Toast.LENGTH_SHORT).show();
            }

        }
        else if(command.size()== 3){
            String com1 = command.get(0).toString();
            String com2 = command.get(1).toString();
            String com3 = command.get(2).toString();

            //time
            Boolean containsTime = com1.compareToIgnoreCase("ሠዓት") == 0 || com1.compareToIgnoreCase("ሰዓት") ==0 || com1.compareToIgnoreCase("ሠአት") ==0 || com1.compareToIgnoreCase("ሰአት")==0;
            Boolean containsTime2 = com2.compareToIgnoreCase("ሠዓት") ==0 || com2.compareToIgnoreCase("ሰዓት") ==0 || com2.compareToIgnoreCase("ሠአት") ==0 || com2.compareToIgnoreCase("ሰአት")==0;
            Boolean containsCallPhrase1 = com1.contains("ደዉ") || com1.contains("ደው") || com1.contains("ደዉል") || com1.contains("ደውል") || com1.contains("ደዉል")|| com1.contains("ደውይ") || com1.contains("ደዉይ") || com1.contains("ደውሉ") || com1.contains("ደዉሉ");
            Boolean containsCallPhrase3 = com3.contains("ደዉ") || com3.contains("ደው") ||com3.contains("ደዉል") || com3.contains("ደውል") || com3.contains("ደዉል")|| com3.contains("ደውይ") || com3.contains("ደዉይ") || com3.contains("ደውሉ") || com3.contains("ደዉሉ");
            Boolean containsMessagePhrase1 = com1.contains("መልክት") || com1.contains("መልእክት") || com1.contains("መልዓክት") || com1.contains("ሜሴጅ") || com1.contains("ሜ\u1224ጅ");
            Boolean containsMessagePhrase2 = com2.contains("መልክት") || com2.contains("መልእክት") || com2.contains("መልዓክት") || com2.contains("ሜሴጅ") || com2.contains("ሜ\u1224ጅ");
            Boolean containsMessagePhrase3 = com3.contains("መልክት") || com3.contains("መልእክት") || com3.contains("መልዓክት") || com3.contains("ሜሴጅ") || com3.contains("ሜ\u1224ጅ");
            Boolean containsMessageSufix1 = com1.contains("ላክ") || com1.contains("ላኪ") || com1.contains("ላኩ");
            Boolean containsMessageSufix2 = com2.contains("ላክ") || com2.contains("ላኪ") || com2.contains("ላኩ");
            Boolean containsMessageSufix3 = com3.contains("ላክ") || com3.contains("ላኪ") || com3.contains("ላኩ");
            Boolean containsEmailPharase1 = com1.compareToIgnoreCase("ኢሜል") == 0;
            Boolean containsEmailPharase2 = com2.compareToIgnoreCase("ኢሜል") == 0;

            //time1
            if((com1.compareToIgnoreCase("ስንት") ==0 || com1.compareToIgnoreCase("ንገሪኝ") ==0 || com1.compareToIgnoreCase("ንገረኝ") ==0) && (containsTime2)){
                tellTime();
            }
            //time2
            else if((com2.compareToIgnoreCase("ስንት") ==0 || com2.compareToIgnoreCase("ንገሪኝ") ==0 || com2.compareToIgnoreCase("ንገረኝ") ==0) && (containsTime)){
                tellTime();
            }
            //call1
            else if(com1.compareToIgnoreCase("ለ") ==0 && containsCallPhrase3){
                amharicName = com2;
                String phone = getContact();
                if(phone != "Unsaved"){
                    makeACall(phone);
                }
                else{
                    Toast.makeText(this,"contact not found" ,Toast.LENGTH_SHORT).show();
                }
            }
            //call2
            else if(containsCallPhrase1 && com2.compareToIgnoreCase("ለ")==0){
                amharicName = com3;
                String phone = getContact();
                if(phone != "Unsaved"){
                    makeACall(phone);
                }
                else{
                    Toast.makeText(this,"contact not found" ,Toast.LENGTH_SHORT).show();
                }
            }
            //message1
            else if(com1.compareToIgnoreCase("ለ")==0 && containsMessagePhrase3){
                if(com2.length()>1){
                    amharicName = com2;
                }
                String phone = getContact();
                if(phone != "Unsaved"){
                    getMessageContent(phone);
                }
                else{
                    Toast.makeText(this,"contact not found" ,Toast.LENGTH_SHORT).show();
                }

            }
            //message2
            else if(containsMessagePhrase1 && com2.compareToIgnoreCase("ለ")==0){
                if(com3.length()>1){
                    amharicName = com3;
                }
                String phone = getContact();
                if(phone != "Unsaved"){
                    getMessageContent(phone);
                }
                else{
                    Toast.makeText(this,"contact not found" ,Toast.LENGTH_SHORT).show();
                }
            }
            //message3
            else if(containsMessagePhrase2 && containsMessageSufix3){
                if(com1.length()>1){
                    amharicName = com1.substring(1);
                }
                String phone = getContact();
                if(phone != "Unsaved"){
                    getMessageContent(phone);
                }
                else{
                    Toast.makeText(this,"contact not found",Toast.LENGTH_SHORT).show();
                }
            }
            //message4
            else if(containsMessagePhrase1 && containsMessageSufix3){
                if(com2.length()>1){
                    amharicName = com2.substring(1);
                }
                String phone = getContact();
                if(phone != "Unsaved"){
                    getMessageContent(phone);
                }
                else{
                    Toast.makeText(this,"contact not found" ,Toast.LENGTH_SHORT).show();
                }
            }
            //email1
            else if(containsEmailPharase1 && containsMessageSufix3){
                if(com2.length()>1){
                    amharicName = com2.substring(1);
                }
                receiverEmail = getEmail();
                if(receiverEmail != "Unsaved"){
                    getEmailContent();
                }
                else{
                    Toast.makeText(this,"contact not found" ,Toast.LENGTH_SHORT).show();
                }
            }
            //email2
            else if(containsEmailPharase2 && containsMessageSufix3){
                if(com2.length()>1){
                    amharicName = com1.substring(1);
                }
                receiverEmail = getEmail();
                if(receiverEmail != "Unsaved"){
                    getEmailContent();
                }
                else{
                    Toast.makeText(this,"contact not found" ,Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this,"len: 3, Command not recognized:" + com2,Toast.LENGTH_SHORT).show();
            }
        }

        else if(command.size() == 4){
            String com1 = command.get(0).toString();
            String com2 = command.get(1).toString();
            String com3 = command.get(2).toString();
            String com4 = command.get(3).toString();

            Boolean containsTime = com1.compareToIgnoreCase("ሠዓት") == 0 || com1.compareToIgnoreCase("ሰዓት") ==0 || com1.compareToIgnoreCase("ሠአት") ==0 || com1.compareToIgnoreCase("ሰአት")==0;
            Boolean containsTime2 = com2.compareToIgnoreCase("ሠዓት") ==0 || com2.compareToIgnoreCase("ሰዓት") ==0 || com2.compareToIgnoreCase("ሠአት") ==0 || com2.compareToIgnoreCase("ሰአት")==0;
            Boolean containsCallPhrase1 = com1.contains("ደዉ") || com1.contains("ደው") || com1.contains("ደዉል") || com1.contains("ደውል") || com1.contains("ደዉል")|| com1.contains("ደውይ") || com1.contains("ደዉይ") || com1.contains("ደውሉ") || com1.contains("ደዉሉ");
            Boolean containsCallPhrase4 = com4.contains("ደዉ") || com4.contains("ደው") ||com4.contains("ደዉል") || com4.contains("ደውል") || com4.contains("ደዉል")|| com4.contains("ደውይ") || com4.contains("ደዉይ") || com4.contains("ደውሉ") || com4.contains("ደዉሉ");
            Boolean containsMessagePhrase1 = com1.contains("መልክት") || com1.contains("መልእክት") || com1.contains("መልዓክት") || com1.contains("ሜሴጅ") || com1.contains("ሜ\u1224ጅ");
            Boolean containsMessagePhrase2 = com2.contains("መልክት") || com2.contains("መልእክት") || com2.contains("መልዓክት") || com2.contains("ሜሴጅ") || com2.contains("ሜ\u1224ጅ");
            Boolean containsMessagePhrase3 = com3.contains("መልክት") || com3.contains("መልእክት") || com3.contains("መልዓክት") || com3.contains("ሜሴጅ") || com3.contains("ሜ\u1224ጅ");
            Boolean containsMessageSufix1 = com1.contains("ላክ") || com1.contains("ላኪ") || com1.contains("ላኩ");
            Boolean containsMessageSufix2 = com2.contains("ላክ") || com2.contains("ላኪ") || com2.contains("ላኩ");
            Boolean containsMessageSufix4 = com4.contains("ላክ") || com4.contains("ላኪ") || com4.contains("ላኩ");

            //time1
            if((com1.compareToIgnoreCase("ስንት") ==0 || com1.compareToIgnoreCase("ንገሪኝ") ==0 || com1.compareToIgnoreCase("ንገረኝ") ==0) && (containsTime2)){
                tellTime();
            }
            //time2
            else if((com2.compareToIgnoreCase("ስንት") ==0 || com2.compareToIgnoreCase("ንገሪኝ") ==0 || com2.compareToIgnoreCase("ንገረኝ") ==0) && (containsTime)){
                tellTime();
            }
            //call1
            else if(com1.compareToIgnoreCase("ለ") ==0 && containsCallPhrase4){
                if(com3.compareToIgnoreCase("ስልክ") ==0){
                    amharicName = com2;
                }
                else{
                    amharicName = com2 + com3;
                }
                String phone = getContact();
                if(phone == "Unsaved"){
                    amharicName = com2 + " "+ com3;
                    phone = getContact();
                }
                if(phone != "Unsaved"){
                    makeACall(phone);
                }
                else{
                    Toast.makeText(this,"contact not found" ,Toast.LENGTH_SHORT).show();
                }

            }
            //call2
            else if(containsCallPhrase1 && com2.compareToIgnoreCase("ለ")==0){
                amharicName = com3 + com4;
                String phone = getContact();
                if(phone == "Unsaved"){
                    amharicName = com3 + " "+ com4;
                    phone = getContact();
                }
                if(phone != "Unsaved"){
                    makeACall(phone);
                }
                else{
                    Toast.makeText(this,"contact not found" ,Toast.LENGTH_SHORT).show();
                }
            }
            //message1
            else if(containsMessagePhrase3 && containsMessageSufix4){
                if(com2.length()>1){
                    amharicName = com2;
                }
                String phone = getContact();
                if(phone != "Unsaved"){
                    getMessageContent(phone);
                }
                else{
                    Toast.makeText(this,"contact not found" ,Toast.LENGTH_SHORT).show();
                }

            }
            //message2
            else if(containsMessagePhrase1 && containsMessageSufix4){
                if(com3.length()>1){
                    amharicName = com3;
                }
                String phone = getContact();
                if(phone != "Unsaved"){
                    getMessageContent(phone);
                }
                else{
                    Toast.makeText(this,"contact not found" ,Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            Toast.makeText(this,"Command not recognized:",Toast.LENGTH_SHORT).show();
        }


        //String phone = getContact();
        //Toast.makeText(this, phone, Toast.LENGTH_SHORT).show();
    }

    public String getContact() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.

            String s = amharicName;
            String contactName = "";
            for (int i =0; i<s.length(); i++){
                String fidel = s.charAt(i)+"";
                if(fidelMap.get(fidel) != null){
                    contactName += fidelMap.get(fidel);
                }
            }
            Log.d(contactName,"test2");

            Log.d(contactName,"contactName");
            String phone = getPhoneNumber(contactName);
            Log.d(phone,"phone number");
            return phone;


        }
        return null;
    }

    public void loadFidelMap(){
        fidelMap.put("ሐ", "ha");
        fidelMap.put("ኀ", "ha");
        fidelMap.put("ሃ", "ha");
        fidelMap.put("ሓ", "ha");
        fidelMap.put("ኃ", "ha");
        fidelMap.put("ሁ", "hu");
        fidelMap.put("ሑ", "hu");
        fidelMap.put("ኁ", "hu");
        fidelMap.put("ሂ", "hi");
        fidelMap.put("ሒ", "hi");
        fidelMap.put("ኂ", "hi");
        fidelMap.put("ሄ", "he");
        fidelMap.put("ሔ", "he");
        fidelMap.put("ኄ", "he");
        fidelMap.put("ህ", "h");
        fidelMap.put("ሕ", "h");
        fidelMap.put("ኅ", "h");
        fidelMap.put("ሆ", "ho");
        fidelMap.put("ሖ", "ho");
        fidelMap.put("ኆ", "ho");
        fidelMap.put("ለ", "le");
        fidelMap.put("ሉ", "lu");
        fidelMap.put("ሊ", "li");
        fidelMap.put("ላ", "la");
        fidelMap.put("ሌ", "le");
        fidelMap.put("ል", "l");
        fidelMap.put("ሎ", "lo");
        fidelMap.put("ሏ", "lwa");
        fidelMap.put("መ", "me");
        fidelMap.put("ሙ", "mu");
        fidelMap.put("ሚ", "mi");
        fidelMap.put("ማ", "ma");
        fidelMap.put("ሜ", "me");
        fidelMap.put("ም", "m");
        fidelMap.put("ሞ", "mo");
        fidelMap.put("ሟ", "mwa");
        fidelMap.put("ሠ", "se");
        fidelMap.put("ሰ", "se");
        fidelMap.put("ሡ", "su");
        fidelMap.put("ሱ", "su");
        fidelMap.put("ሢ", "si");
        fidelMap.put("ሲ", "si");
        fidelMap.put("ሣ", "sa");
        fidelMap.put("ሳ", "sa");
        fidelMap.put("ሤ", "se");
        fidelMap.put("ሴ", "se");
        fidelMap.put("ሥ", "s");
        fidelMap.put("ስ", "s");
        fidelMap.put("ሦ", "so");
        fidelMap.put("ሶ", "so");
        fidelMap.put("ረ", "re");
        fidelMap.put("ሩ", "ru");
        fidelMap.put("ሪ", "ri");
        fidelMap.put("ራ", "ra");
        fidelMap.put("ሬ", "re");
        fidelMap.put("ር", "r");
        fidelMap.put("ሮ", "ro");
        fidelMap.put("ሸ", "she");
        fidelMap.put("ሹ", "shu");
        fidelMap.put("ሺ", "shi");
        fidelMap.put("ሻ", "sha");
        fidelMap.put("ሼ", "she");
        fidelMap.put("ሽ", "sh");
        fidelMap.put("ሾ", "sho");
        fidelMap.put("ቀ", "qe");
        fidelMap.put("ቁ", "qu");
        fidelMap.put("ቂ", "qi");
        fidelMap.put("ቃ", "qa");
        fidelMap.put("ቄ", "qe");
        fidelMap.put("ቅ", "q");
        fidelMap.put("ቆ", "qo");
        fidelMap.put("ቋ", "qwa");
        fidelMap.put("በ", "be");
        fidelMap.put("ቡ", "bu");
        fidelMap.put("ቢ", "bi");
        fidelMap.put("ባ", "ba");
        fidelMap.put("ቤ", "be");
        fidelMap.put("ብ", "b");
        fidelMap.put("ቦ", "bo");
        fidelMap.put("ተ", "te");
        fidelMap.put("ቱ", "tu");
        fidelMap.put("ቲ", "ti");
        fidelMap.put("ታ", "ta");
        fidelMap.put("ቴ", "te");
        fidelMap.put("ት", "t");
        fidelMap.put("ቶ", "to");
        fidelMap.put("ቷ", "twa");
        fidelMap.put("ቸ", "che");
        fidelMap.put("ቹ", "chu");
        fidelMap.put("ቺ", "chi");
        fidelMap.put("ቻ", "cha");
        fidelMap.put("ቼ", "che");
        fidelMap.put("ች", "ch");
        fidelMap.put("ቾ", "cho");
        fidelMap.put("ነ", "ne");
        fidelMap.put("ኑ", "nu");
        fidelMap.put("ኒ", "ni");
        fidelMap.put("ና", "na");
        fidelMap.put("ኔ", "ne");
        fidelMap.put("ን", "n");
        fidelMap.put("ኖ", "no");
        fidelMap.put("ኘ", "gne");
        fidelMap.put("ኙ", "gnu");
        fidelMap.put("ኚ", "gni");
        fidelMap.put("ኛ", "gna");
        fidelMap.put("ኜ", "gne");
        fidelMap.put("ኝ", "gn");
        fidelMap.put("ኞ", "gno");
        fidelMap.put("አ", "a");
        fidelMap.put("ዐ", "a");
        fidelMap.put("ኡ", "u");
        fidelMap.put("ዑ", "u");
        fidelMap.put("ኢ", "i");
        fidelMap.put("ዒ", "i");
        fidelMap.put("ኣ", "a");
        fidelMap.put("ዓ", "a");
        fidelMap.put("ኤ", "e");
        fidelMap.put("ዔ", "e");
        fidelMap.put("እ", "e");
        fidelMap.put("ዕ", "e");
        fidelMap.put("ኦ", "o");
        fidelMap.put("ዖ", "o");
        fidelMap.put("ከ", "ke");
        fidelMap.put("ኩ", "ku");
        fidelMap.put("ኪ", "ki");
        fidelMap.put("ካ", "ka");
        fidelMap.put("ኬ", "ke");
        fidelMap.put("ክ", "k");
        fidelMap.put("ኮ", "ko");
        fidelMap.put("ኸ", "he");
        fidelMap.put("ኹ", "hu");
        fidelMap.put("ኺ", "hi");
        fidelMap.put("ኻ", "ha");
        fidelMap.put("ኼ", "he");
        fidelMap.put("ኽ", "h");
        fidelMap.put("ኾ", "ho");
        fidelMap.put("ወ", "we");
        fidelMap.put("ዊ", "wi");
        fidelMap.put("ዋ", "wa");
        fidelMap.put("ዌ", "we");
        fidelMap.put("ው", "wu");
        fidelMap.put("ዉ", "w");
        fidelMap.put("ዎ", "wo");
        fidelMap.put("ዘ", "ze");
        fidelMap.put("ዙ", "zu");
        fidelMap.put("ዚ", "zi");
        fidelMap.put("ዛ", "za");
        fidelMap.put("ዜ", "ze");
        fidelMap.put("ዝ", "z");
        fidelMap.put("ዞ", "zo");
        fidelMap.put("ዠ", "ža");
        fidelMap.put("ዡ", "žu");
        fidelMap.put("ዢ", "ži");
        fidelMap.put("ዣ", "ža");
        fidelMap.put("ዤ", "že");
        fidelMap.put("ዥ", "ž");
        fidelMap.put("ዦ", "žo");
        fidelMap.put("የ", "ye");
        fidelMap.put("ዩ", "yu");
        fidelMap.put("ዪ", "yi");
        fidelMap.put("ያ", "ya");
        fidelMap.put("ዬ", "ye");
        fidelMap.put("ይ", "y");
        fidelMap.put("ዮ", "yo");
        fidelMap.put("ደ", "de");
        fidelMap.put("ዱ", "du");
        fidelMap.put("ዲ", "di");
        fidelMap.put("ዳ", "da");
        fidelMap.put("ዴ", "de");
        fidelMap.put("ድ", "d");
        fidelMap.put("ዶ", "do");
        fidelMap.put("ጀ", "je");
        fidelMap.put("ጁ", "ju");
        fidelMap.put("ጂ", "gi");
        fidelMap.put("ጃ", "ja");
        fidelMap.put("ጄ", "ge");
        fidelMap.put("ጅ", "g");
        fidelMap.put("ጆ", "go");
        fidelMap.put("ገ", "ge");
        fidelMap.put("ጉ", "gu");
        fidelMap.put("ጊ", "gi");
        fidelMap.put("ጋ", "ga");
        fidelMap.put("ጌ", "ge");
        fidelMap.put("ግ", "g");
        fidelMap.put("ጎ", "go");
        fidelMap.put("ጐ", "gu");
        fidelMap.put("ጓ", "ga");
        fidelMap.put("ጠ", "te");
        fidelMap.put("ጡ", "tu");
        fidelMap.put("ጢ", "ti");
        fidelMap.put("ጣ", "ta");
        fidelMap.put("ጤ", "te");
        fidelMap.put("ጥ", "t");
        fidelMap.put("ጦ", "to");
        fidelMap.put("ጨ", "che");
        fidelMap.put("ጩ", "chu");
        fidelMap.put("ጪ", "chi");
        fidelMap.put("ጫ", "cha");
        fidelMap.put("ጬ", "che");
        fidelMap.put("ጭ", "ch");
        fidelMap.put("ጮ", "cho");
        fidelMap.put("ጰ", "pe");
        fidelMap.put("ጱ", "pu");
        fidelMap.put("ጲ", "pi");
        fidelMap.put("ጳ", "pa");
        fidelMap.put("ጴ", "pe");
        fidelMap.put("ጵ", "p");
        fidelMap.put("ጶ", "po");
        fidelMap.put("ጸ", "tse");
        fidelMap.put("ፀ", "tse");
        fidelMap.put("ጹ", "tsu");
        fidelMap.put("ፁ", "tsu");
        fidelMap.put("ጺ", "tsi");
        fidelMap.put("ፂ", "tsi");
        fidelMap.put("ጻ", "tsa");
        fidelMap.put("ፃ", "tsa");
        fidelMap.put("ጼ", "tse");
        fidelMap.put("ፄ", "tse");
        fidelMap.put("ጽ", "ts");
        fidelMap.put("ፅ", "ts");
        fidelMap.put("ጾ", "tso");
        fidelMap.put("ፆ", "tso");
        fidelMap.put("ፈ", "fe");
        fidelMap.put("ፉ", "fu");
        fidelMap.put("ፊ", "fi");
        fidelMap.put("ፋ", "fa");
        fidelMap.put("ፌ", "fe");
        fidelMap.put("ፍ", "f");
        fidelMap.put("ፎ", "fo");
        fidelMap.put("ፐ", "pe");
        fidelMap.put("ፑ", "pu");
        fidelMap.put("ፒ", "pi");
        fidelMap.put("ፓ", "pa");
        fidelMap.put("ፔ", "pe");
        fidelMap.put("ፕ", "p");
        fidelMap.put("ፖ", "po");

    }

    public String getPhoneNumber(String name) {
        String ret = null;

        String where = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " like '%";
        //String selection = name;
        where = where + name+"%'";
        //String[] selectionArgs = {selection};

        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, where, null, null);
        if (c.moveToLast()) {
            ret = c.getString(0);
        }
        c.close();
        if (ret == null) {
            ret = "Unsaved";
        }
        return ret;
    }

    public String getEmail() {
        String ret = null;

        String s = amharicName;
        String name = "";
        for (int i =0; i<s.length(); i++){
            String fidel = s.charAt(i)+"";
            if(fidelMap.get(fidel) != null){
                contactName += fidelMap.get(fidel);
            }
        }

        String where = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " like '";
        //String selection = name;
        where = where + name+"%'";
        //String[] selectionArgs = {selection};

        String[] projection = new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS};
        Cursor c = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, where, null, null);
        if (c.moveToLast()) {
            ret = c.getString(0);
        }
        c.close();
        if (ret == null) {
            ret = "Unsaved";
        }
        return ret;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                getContact();
            } else {
                Toast.makeText(this, "Until you grant the permission, we can not access contacts", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                prepareSmsManager();
            } else {
                Toast.makeText(this, "Until you grant the permission, we can not send SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void call(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);

    }

    public void makeACall(String phone){
        Toast.makeText(this, "phone number: "+phone,Toast.LENGTH_SHORT).show();
        call(phone);
    }

    public String tellTime(){
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Toast.makeText(this,"Time & Date: "+currentDateTimeString,Toast.LENGTH_SHORT).show();
        return null;
    }

    public void sendMessage(View view){
        prepareSmsManager();
    }

    public void getCallerName(){
        setContentView(R.layout.contact);
    }

    public void getCallerPhone(View view){
        amharicName = ((EditText)findViewById(R.id.contactInputText)).getText().toString().trim();
        String number = getContact();
        contactNumber = number;

        if(isCall){
            //make the actual call
            makeACall(number);
        }
        else if(isMail){
            //
        }
        else{
            getMessageContent(number);
        }

    }

    public void getMessageContent(String phone){
        setContentView(R.layout.message_content);
        contactNumber = phone;
        Toast.makeText(this, "phone: "+phone, Toast.LENGTH_SHORT).show();
    }

    public void getEmailContent(){
        setContentView(R.layout.mailer);
        //Toast.makeText(this, "phone: "+phone, Toast.LENGTH_SHORT).show();
    }

    public void prepareSmsManager(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        else {
            SmsManager smsManager = SmsManager.getDefault();
            String content = ((EditText)findViewById(R.id.contentInput)).getText().toString();
            Log.i("Send SMS", "");
            smsManager.sendTextMessage(contactNumber, null, content, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        }

    }

    public void prepareSms(){
        String content = ((EditText)findViewById(R.id.contentInput)).getText().toString();
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , contactNumber);
        smsIntent.putExtra("sms_body"  , content);

        try {
            startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    public void backAction(View view){
        setContentView(R.layout.activity_main);
    }

    protected void sendEmail(View view) {
        receiverEmail= (((EditText)findViewById(R.id.emailText)).getText()).toString();
        Toast.makeText(MainActivity.this, ""+receiverEmail, Toast.LENGTH_SHORT).show();
        subject = ((EditText)findViewById(R.id.subjectText)).getText().toString();
        body = ((EditText)findViewById(R.id.bodyText)).getText().toString();

        Log.i("Send email", "");
        String[] TO = {receiverEmail};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);



        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);


        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    public void flip1(View view){
        ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        receiverEmail= ((EditText)findViewById(R.id.emailText)).getText().toString();
        viewFlipper.showNext();
    }
    public void flip2(View view){
        ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        subject = ((EditText)findViewById(R.id.subjectText)).getText().toString();
        viewFlipper.showNext();
    }

    public void sendEmail2(View view){

        body = ((EditText)findViewById(R.id.bodyText)).getText().toString();


            //Toast.makeText(this, "SendMail" ,Toast.LENGTH_LONG).show();

            new Thread(new Runnable() {
            public void run() {
                // a potentially  time consuming task
                sender = new GMailSender(senderEmail, "Megatron0");
                try {   sender.sendMail(subject,
                        body, senderEmail,receiverEmail);
                    toastLong("ኢሜሉ ተልኩአል");
                } catch (Exception e) {
                    toastLong("ኢሜሉ አልተላከም");
                    Log.d("SendMail", e.getMessage(), e);
                }
            }
        }).start();
//

    }

    public void changeLayout(int layout){
        setContentView(layout);
    }

    public void executeAlarm(View view){
        EditText AlarmInput = (EditText) findViewById(R.id.alarmTimeText);
        String str = AlarmInput.getText().toString().trim().replaceAll("[.]", "");

        ArrayList command = new ArrayList();
        //command.add(str.charAt(0)+"");
        String arg = "";
        for(int i=0; i<str.length(); i++){
            if(str.charAt(i) == ' '){
                if(arg != ""){
                    Log.d(arg,"test");

                    command.add(arg.trim());
                }
                arg = "";
            }
            arg = arg + str.charAt(i)+"";
            if(i == str.length()-1){
                if(arg !=""){

                    command.add(arg.trim());
                }
            }
        }
        AlarmSetter setter = new AlarmSetter(this);
        int len = command.size();
        int h = 8;
        int m = 0;
        String AM_PM = "AM";
        String dayText = "today";

        if(len == 1){
            int hour = setter.getLastNumbers(command.get(0).toString());
            if(hour !=-1){
                h = hour;
            }
            Log.d("alarm: ","WC: "+1+" "+h+":"+m+ " "+AM_PM);

            Toast.makeText(this,"alarm: WC: "+1+" "+h+":"+m+ " "+AM_PM, Toast.LENGTH_SHORT).show();
            //setter.setAlarm(h, 0 , "AM", "");
        }

        else if(len == 2){

            int hour = setter.getLastNumbers(command.get(0).toString());
            if(hour == -1){
                hour = setter.getFirstNumbers(command.get(0).toString());

                if(hour != -1){
                    int hour2 = setter.getLastNumbers(command.get(1).toString());
                    if(hour2 != -1){
                        hour = hour + hour2;
                        h = hour;
                    }
                    else{
                        h = hour;

                    }
                }
            }
            else{
                h = hour;
                String s = command.get(1).toString();
                if(s.contains("ሰዓት") || s.contains("ሰአት")){

                }
                else{
                    //Toast.makeText(this,"test",Toast.LENGTH_SHORT).show();
                    int minutes = setter.getLastNumbers(command.get(1).toString());
                    if(minutes != -1){
                       m = minutes;
                    }
                }
            }
            Log.d("alarm: ","WC: "+2+" "+h+":"+m+ " "+AM_PM);
            Toast.makeText(this,"alarm: WC: "+2+" "+h+":"+m+ " "+AM_PM, Toast.LENGTH_SHORT).show();
            //setter.setAlarm(h,m,AM_PM,dayText);
        }
        else if(len == 3){
            //check to see if the day is given
            boolean day1 = setter.getDayText(command.get(0).toString()) != "";
            boolean day2 = setter.getDayText(command.get(1).toString()) != "";
            boolean day3 = setter.getDayText(command.get(2).toString()) != "";
            String day = "";
            if(day1 || day2 || day3){
                int dayTextIndex = 0;
                if(day1){
                    dayTextIndex = 0;
                }
                else if(day2){
                    dayTextIndex = 1;
                }
                else if(day3){
                    dayTextIndex = 2;
                }
                day = command.get(dayTextIndex).toString();
                dayText = setter.getDayText(day);
                command.remove(dayTextIndex);

                //check to see if "ሰዓት' included
                String text1 = command.get(0).toString();
                String text2 = command.get(1).toString();
                //boolean b1 = (text1.contains("ሰዓት") || text1.contains("ሰአት"));
                boolean b2 = (text2.contains("ሰዓት") || text2.contains("ሰአት"));

                if(b2){
                    h = setter.getLastNumbers(command.get(0).toString());
                }
            }
            else{

                //check to see if "ሰዓት' included
                String text1 = command.get(0).toString();
                String text2 = command.get(1).toString();
                String text3 = command.get(2).toString();
                boolean b1 = (text1.contains("ሰዓት") || text1.contains("ሰአት"));
                boolean b2 = (text2.contains("ሰዓት") || text2.contains("ሰአት"));
                boolean b3 = (text3.contains("ሰዓት") || text3.contains("ሰአት"));

                int removeIndex = -1;
                if(b1){
                    removeIndex = 0;
                }
                else if(b2){
                    removeIndex = 1;
                }
                else if(b3){
                    removeIndex = 2;
                }

                if(removeIndex != -1){
                    command.remove(removeIndex);
                    int hour = setter.getLastNumbers(command.get(0).toString());
                    if(hour !=-1){
                        h = hour;
                    }
                    else{

                        int hour2 = setter.getFirstNumbers(command.get(0).toString());
                        if(hour2 != -1){

                            hour = setter.getLastNumbers(command.get(1).toString());
                            if(hour !=-1){
                                h = hour + hour2;
                            }
                        }
                        else{
                            hour = setter.getLastNumbers(command.get(1).toString());
                            if(hour !=-1){
                                h = hour;
                            }
                        }

                    }
                    int minutes = setter.getLastNumbers(command.get(1).toString());
                    if(minutes != -1){
                        m = minutes;
                    }

                    String am_pm = setter.getPM_AM(command.get(1).toString());
                    if(am_pm != "MM"){
                        AM_PM = am_pm;
                    }
                    am_pm = setter.getPM_AM(command.get(0).toString());
                    if(am_pm != "MM"){
                        AM_PM = am_pm;
                    }

                }
                //no hour label
                else{
                    int minutes = setter.getLastNumbers(command.get(2).toString());
                    if(minutes != -1 && !command.get(1).toString().contains("ጉዳይ")){
                        m = minutes;
                        command.remove(2);
                        String am_pm = setter.getPM_AM(command.get(0).toString());
                        if(am_pm != "MM"){
                            AM_PM = am_pm;
                        }
                        int hour2 = setter.getFirstNumbers(command.get(0).toString());
                        if(hour2!=-1){
                            int hour = setter.getLastNumbers(command.get(1).toString());
                            if(hour!=-1){
                                h = hour + hour2;
                            }
                        }

                    }
                    else {
                        minutes = setter.getLastNumbers(command.get(1).toString());
                        if(minutes != -1 && !command.get(1).toString().contains("ጉዳይ")){
                            m = minutes;
                            int hour = setter.getLastNumbers(command.get(0).toString());
                            if(hour!=-1){
                                h = hour;
                            }
                            String am_pm = setter.getPM_AM(command.get(0).toString());
                            if(am_pm != "MM"){
                                AM_PM = am_pm;
                            }
                        }
                        String am_pm = setter.getPM_AM(command.get(0).toString());
                        if(am_pm != "MM"){
                            AM_PM = am_pm;
                            int hour2 = setter.getFirstNumbers(command.get(1).toString());
                            if(hour2!=-1){
                                int hour = setter.getLastNumbers(command.get(2).toString());
                                if(hour!=-1){
                                    h = hour + hour2;
                                }
                            }
                        }
                        else{
                            minutes = setter.getLastNumbers(command.get(0).toString());
                            if(minutes != -1){
                                if((command.get(1).toString()).contains("ጉዳይ")){
                                    int hour = setter.getLastNumbers(command.get(2).toString());
                                    if(hour!=-1){
                                        h = hour -1;
                                        m = 30+minutes;
                                    }
                                }
                            }
                        }

                    }


                }


            }
            Log.d("alarm: ","WC: "+2+" "+h+":"+m+ " "+AM_PM);
            Toast.makeText(this,"alarm: WC: "+3+"  "+h+":"+m+ " "+AM_PM, Toast.LENGTH_SHORT).show();
            //setter.setAlarm(h,m,AM_PM,"");

        }
        else if(len == 4){

        }


    }
    public ArrayList getInputArray(String str){
        ArrayList command = new ArrayList();
        //command.add(str.charAt(0)+"");
        String arg = "";
        for(int i=0; i<str.length(); i++){
            if(str.charAt(i) == ' '){
                if(arg != ""){
                    Log.d(arg,"test");

                    command.add(arg.trim());
                }
                arg = "";
            }
            arg = arg + str.charAt(i)+"";
            if(i == str.length()-1){
                if(arg !=""){

                    command.add(arg.trim());
                }
            }
        }
        return command;
    }

    public void getNewContactPhone(View view) throws RemoteException, OperationApplicationException {

        String phoneInput = ((EditText)findViewById(R.id.phoneText)).getText().toString();
        ArrayList command = getInputArray(phoneInput);

        NewContact contact = new NewContact(this);
        newContactPhone = contact.convertNumbers(command);
        //Toast.makeText(this,"phone: "+phoneInput, Toast.LENGTH_LONG).show();
        setContentView(R.layout.new_contact_save);

    }
    public void saveContact(View view) throws RemoteException, OperationApplicationException {
        String nameInput = ((EditText)findViewById(R.id.nameText)).getText().toString();
        if(nameInput != ""){
            new NewContact(this).addContact(nameInput, newContactPhone);
            Toast.makeText(this,"Contact Saved!", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_main);
        }

    }


}
















