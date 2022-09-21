package com.table.schedule.DBeditor;

public class ModelPass {
    private Dao Generator;
    public ModelPass(int typeWork)
    {
        switch (typeWork)
        {
            case 1:Generator=new DBConnection();
            default:Generator=new DBConnection();
        }
        Generator.getDBConnection();
    }

    public Dao getGenerator() {
        return Generator;
    }
}
