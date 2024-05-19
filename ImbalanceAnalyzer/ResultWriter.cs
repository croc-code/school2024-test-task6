namespace ImbalanceAnalyzer;

public class ResultWriter
{
    public void Write(StreamWriter writer, List<Result> results)
    {
        var pluses = results
            .Where(record => record.Hours > 0)
            .OrderBy(x => x.Name)
            .ToList();
        
        var minuses = results
            .Where(record => record.Hours < 0)
            .OrderBy(x => x.Name)
            .ToList();

        foreach (var record in minuses)
        {
            writer.WriteLine($"{record.Name} {record.Hours}");
        }
        
        foreach (var record in pluses)
        {
            writer.WriteLine($"{record.Name} +{record.Hours}");
        }
    }
}