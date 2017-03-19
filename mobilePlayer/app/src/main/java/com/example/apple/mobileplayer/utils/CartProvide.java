package com.example.apple.mobileplayer.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.example.apple.mobileplayer.domain.ShoppingCart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 17/3/17.
 */

public class CartProvide {

    public static final String JSON_CART = "json_cart";
    private Context context;
    /**
     * SparseArray替换hashMap ，性能好于hashMap
     */
    private SparseArray<ShoppingCart> datas;

    private CartProvide(Context context) {

        this.context = context;
        datas = new SparseArray<>(10);

        listToSparse();


    }

    private void listToSparse() {

        List<ShoppingCart> carts = getAllData();
        if (carts != null && carts.size() > 0) {

            for (int i = 0; i < carts.size(); i++) {

                ShoppingCart cart = carts.get(i);
                datas.put(cart.getId(), cart);

            }

        }

    }

    /**
     * 得到所有数据
     *
     * @return
     */
    private List<ShoppingCart> getAllData() {

        return getDataFromLocal();
    }

    /**
     * 从本地获取json数据，并且通过gson解析成列表数据
     *
     * @return
     */
    private List<ShoppingCart> getDataFromLocal() {

        List<ShoppingCart> carts = new ArrayList<>();

        //从本地获取缓存的数据
        String saveJson = CacheUtils.getString(context, JSON_CART);

        if (!TextUtils.isEmpty(saveJson)) {
            //通过gson把数据转换城list列表
            carts = new Gson().fromJson(saveJson, new TypeToken<List<ShoppingCart>>() {}.getType());
        }

        return carts;
    }

    /**
     * 增加数据
     *
     * @param cart
     */
    public void addData(ShoppingCart cart) {

        //1.添加数据
        ShoppingCart tempCart = datas.get(cart.getId());
        if (tempCart != null) {
            //列表中已经存在该条数据
            tempCart.setCount(tempCart.getCount() + 1);
        } else {
            tempCart = cart;
            tempCart.setCount(1);
        }

        datas.put(tempCart.getId(), tempCart);

        //2、保存数据
        commit();
    }

    /**
     * 删除数据数据
     *
     * @param cart
     */
    public void deleteData(ShoppingCart cart) {

        //1.删除数据
        datas.delete(cart.getId());

        //2、保存数据
        commit();
    }


    /**
     * 修改数据数据
     *
     * @param cart
     */
    public void updateData(ShoppingCart cart) {

        //1.修改数据
        datas.put(cart.getId(), cart);

        //2、保存数据
        commit();
    }

    /**
     * 保存数据
     */
    private void commit() {

        //1、把sparseArray转换成list
        List<ShoppingCart> carts = parsesToList();

        //2、用gson把list转换成string
        String json = new Gson().toJson(carts);

        //3、保存数据
        CacheUtils.putString(context,JSON_CART,json);

    }

    /**
     * 从parses的数据转换成list列表数据
     */
    private List<ShoppingCart> parsesToList() {

        List<ShoppingCart> carts = new ArrayList<>();
        if (datas != null && datas.size()>0){

            for (int i=0;i<datas.size();i++){

                ShoppingCart cart = datas.valueAt(i);
                carts.add(cart);
            }
        }
        return carts;

    }

}
