namespace ImbalanceAnalyzer;

public class ResultRecord
{
    public ResultRecord(string name, float hours)
    {
        Name = name;
        Hours = hours;
    }

    public string Name { get; set; }
    public float Hours { get; set; }
    
    
}
