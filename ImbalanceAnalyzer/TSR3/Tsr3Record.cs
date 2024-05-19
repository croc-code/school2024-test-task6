namespace ImbalanceAnalyzer;

public class Tsr3Record
{
    public Guid Uuid { get; set; }
    public string Name { get; set; }
    public DateTime DebitingDate { get; set; }  
    public float DebitingHours { get; set; }

    public Tsr3Record(Guid uuid, string name, DateTime debitingDate, float debitingHours)
    {
        Uuid = uuid;
        Name = name;
        DebitingDate = debitingDate;
        DebitingHours = debitingHours;
    }
}