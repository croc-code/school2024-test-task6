namespace ImbalanceAnalyzer;

public class ResultCompiler
{
    public List<ResultRecord> Compile(ParsedTsr3 file)
    {
        float standard = file.Standard;
        var map = new Dictionary<Guid, ResultRecord>();

        foreach (var fileRecord in file.Records)
        {
            if (!map.TryAdd(fileRecord.Uuid, new ResultRecord(fileRecord.Name, -24f + fileRecord.DebitingHours)))
            {
                map[fileRecord.Uuid].Hours += fileRecord.DebitingHours;
            }
        }

        foreach (var pair in map)
        {
            var percent = Math.Abs(pair.Value.Hours) / standard;
            
            if ( percent <= 0.1f)
            {
                map.Remove(pair.Key);
            }
        }

        return map.Select(pair => pair.Value).ToList();
    }
}