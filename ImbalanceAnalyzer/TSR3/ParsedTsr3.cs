namespace ImbalanceAnalyzer;

public class ParsedTsr3
{
    public float Standard { get; set; }
    public IReadOnlyList<Tsr3Record> Records { get; set; }

    public ParsedTsr3(float standard, List<Tsr3Record> records)
    {
        Standard = standard;
        Records = records;
    }
}