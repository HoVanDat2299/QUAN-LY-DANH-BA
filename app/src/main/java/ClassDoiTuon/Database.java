package ClassDoiTuon;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {


    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //Thực thi lệnh SQL: Create, insert,update,delete
    public void QueryData(String sqlCommand){
        SQLiteDatabase database=getWritableDatabase();
        database.execSQL(sqlCommand);

    }
    // Insert
    public void insertDataDanhBa(String ten, String sodienthoai, byte[] hinhanh, String Email, String DiaChi, String ngaysinh)
    {
        SQLiteDatabase database=getWritableDatabase();
        String sql="INSERT INTO DanhBa VALUES(null,?,?,?,?,?,?,0)";
        SQLiteStatement statement=database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,ten);
        statement.bindString(2,sodienthoai);
        statement.bindBlob(3,hinhanh);
        statement.bindString(4,ngaysinh);
        statement.bindString(5,Email);
        statement.bindString(6,DiaChi);
        statement.executeInsert();

    }



    public void updateDataDanhBa(String ten, String sodienthoai, byte[] hinhanh, String Email, String DiaChi, String ngaysinh, int id)
    {
        SQLiteDatabase database=getWritableDatabase();
        String sql="UPDATE DanhBa SET Ten=?, SoDienThoai=?, HinhAnh=?, NgaySinh=?,DiaChi=?,Email=? WHERE ID=?";
        SQLiteStatement statement=database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,ten);
        statement.bindString(2,sodienthoai);
        statement.bindBlob(3,hinhanh);
        statement.bindString(4,ngaysinh);
        statement.bindString(5,Email);
        statement.bindString(6,DiaChi);
        statement.bindLong(7,id);

        statement.executeUpdateDelete();

    }

    //Truy vấn select
    public Cursor GetData(String sqlCommand){
        SQLiteDatabase database=getReadableDatabase();
        return database.rawQuery(sqlCommand,null);
    }
    //

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
