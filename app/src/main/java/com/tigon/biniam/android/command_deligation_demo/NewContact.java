package com.tigon.biniam.android.command_deligation_demo;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;

import java.util.ArrayList;

/**
 * Created by USER on 5/16/2017.
 */

public class NewContact {
    Context context;
    public NewContact(Context context){
        this.context = context;
    }
    public void addContact(String name, String phone) throws RemoteException, OperationApplicationException {

        WritePhoneContact(name,phone,context);
//        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
//        int rawContactInsertIndex = ops.size();
//
//        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
//
//        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex).withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE).withValue(Phone.NUMBER, "1-800-00-411")
//                .withValue(StructuredName.DISPLAY_NAME, "test2").build());
//        context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);

//        ArrayList<ContentProviderOperation> ops =
//                new ArrayList<ContentProviderOperation>();
//
//        ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
//                .withValue(Data.RAW_CONTACT_ID, ops.size())
//                .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
//                .withValue(Phone.NUMBER, "1-800-GOOG-411")
//                .withValue(Phone.TYPE, Phone.TYPE_CUSTOM)
//                .withValue(Phone.LABEL, "free directory assistance")
//                .build());
//        context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);

    }
//    public void addContact1(String name, String phone){
//        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
//        int rawContactInsertIndex = ops.size();
//        ContentValues values = new ContentValues();
//        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex);
//        values.put(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
//        values.put(Phone.NUMBER, phone);
//        values.put(Phone.TYPE, Phone.TYPE_CUSTOM);
//        values.put(Phone.LABEL, name);
//        Uri dataUri = context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
//    }

    public void WritePhoneContact(String displayName, String number,Context cntx /*App or Activity Ctx*/)
    {
        Context contetx 	= cntx; //Application's context or Activity's context
        String strDisplayName 	=  displayName; // Name of the Person to add
        String strNumber 	=  number; //number of the person to add with the Contact

        ArrayList<ContentProviderOperation> cntProOper = new ArrayList<ContentProviderOperation>();
        int contactIndex = cntProOper.size();//ContactSize

        //Newly Inserted contact
        // A raw contact will be inserted ContactsContract.RawContacts table in contacts database.
        cntProOper.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)//Step1
                .withValue(RawContacts.ACCOUNT_TYPE, null)
                .withValue(RawContacts.ACCOUNT_NAME, null).build());

        //Display name will be inserted in ContactsContract.Data table
        cntProOper.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)//Step2
                .withValueBackReference(Data.RAW_CONTACT_ID,contactIndex)
                .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                .withValue(StructuredName.DISPLAY_NAME, strDisplayName) // Name of the contact
                .build());
        //Mobile number will be inserted in ContactsContract.Data table
        cntProOper.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)//Step 3
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,contactIndex)
                .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                .withValue(Phone.NUMBER, strNumber) // Number to be added
                .withValue(Phone.TYPE, Phone.TYPE_MOBILE).build()); //Type like HOME, MOBILE etc
        try
        {
            // We will do batch operation to insert all above data
            //Contains the output of the app of a ContentProviderOperation.
            //It is sure to have exactly one of uri or count set
            ContentProviderResult[] contentProresult = null;
            contentProresult = contetx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, cntProOper); //apply above data insertion into contacts list
        }
        catch (RemoteException exp)
        {
            //logs;
        }
        catch (OperationApplicationException exp)
        {
            //logs
        }
    }
    public int getNumbers(String s){
        int num = 0;
        if(s.contains("አስራ") || s.contains("ዓስራ") || s.contains("ዐስራ") ){
            num = 10;
        }
        else if(s.contains("ሃያ") || s.contains("ሀያ") || s.contains("ሐያ") ){
            num = 20;
        }
        else if(s.contains("ሰላሳ") || s.contains("ሠላሳ") || s.contains("ሰላሣ") || s.contains("ሠላሣ")){
            num = 30;
        }
        else if(s.contains("አርባ") || s.contains("ዐርባ") || s.contains("ዓርባ")){
            num = 40;
        }
        else if(s.contains("ሃምሳ") || s.contains("ሀምሳ") || s.contains("ሐምሳ") || s.contains("ሀምሣ") || s.contains("ሃምሣ")){
            num = 50;
        }
        else if(s.contains("ስልሳ") || s.contains("ስልሣ") ){
            num = 60;
        }
        else if(s.contains("ሰባ") || s.contains("ሠባ") ){
            num = 70;
        }
        else if(s.contains("ሰማንያ") || s.contains("ሰማኒያ") ){
            num = 80;
        }
        else if(s.contains("ዘጠና")){
            num = 90;
        }
        else{
            num = -1;
        }

        return num;
    }
    public int getLastNumbers(String s){
        if(s.contains("አስር")){
            return 10;
        }
        else if(s.contains("ዘጠኝ")){
            return 9;
        }
        else if(s.contains("ስምንት")){
            return 8;
        }
        else if(s.contains("ሰባት")){
            return 7;
        }
        else if(s.contains("ስድስት")){
            return 6;
        }
        else if(s.contains("አምስት")){
            return 5;
        }
        else if(s.contains("አራት")){
            return 4;
        }
        else if(s.contains("ሶስት")){
            return 3;
        }
        else if(s.contains("ሁለት")){
            return 2;
        }
        else if(s.contains("አንድ")){
            return 1;
        }
        else if(s.contains("ዜሮ")){
            return 0;
        }
        else{
            return -1;
        }
    }
    public String convertNumbers(ArrayList list){
        int index = 0;
        for(int i=0; i<list.size(); i++){
            if(list.get(i).toString().contains("መዝግ") || list.get(i).toString().contains("ሴቭ") || list.get(i).toString().contains("አስቀም")){
                index = i;
            }
        }
        String phone = "";
        for(int i=0; i<list.size(); i++){
            int temp1 = getNumbers(list.get(i).toString());
            if(temp1 != -1){
                if((i+1) < list.size()){
                    int temp2 = getLastNumbers(list.get(i+1).toString());
                    if(temp2 != -1){
                        int p = temp1 + temp2;
                        phone = phone + p;
                        i++;
                    }
                    else {
                        phone = phone + temp1;
                    }
                }
                else{
                    phone = phone +temp1;
                }

            }
            else {
                temp1 = getLastNumbers(list.get(i).toString());
                if(temp1 != -1){
                    phone = phone +temp1;
                }
            }

        }
        return phone;
    }
}
