package app.gamezoptest.gamezoptest.interfaces;
/*
 * Created by Han
 *Vamos!
 *
 */

import java.util.ArrayList;
import java.util.HashMap;

import app.gamezoptest.gamezoptest.Models.AdapterModel;

public interface GetResponse {
    void afterOperation(ArrayList<AdapterModel> imagesURL);
    void getHashMapafterOperation(HashMap<Integer, String> hashMap);
}
