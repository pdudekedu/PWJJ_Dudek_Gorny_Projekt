package WorkManager.Data.QueryBuilders;

import WorkManager.Data.Models.EFModel;
import WorkManager.Data.Models.IInsertable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InsertBuilder {

    public String GetQuery(IInsertable entity)
    {
        if(entity == null)
            return null;
        return String.format("INSERT INTO %s (%s) VALUES (%s)",
                    Into,
                    String.join(", ", entity.getInsertColumns()),
                    String.join(", ", entity.getInsertValues()));
    }

    public InsertBuilder Into(DBTable table)
    {
        Into = table;
        return this;
    }

    protected DBTable Into = null;
}
