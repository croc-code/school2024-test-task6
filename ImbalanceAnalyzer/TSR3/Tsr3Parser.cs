using System.ComponentModel;
using System.Globalization;

namespace ImbalanceAnalyzer;

public class Tsr3Parser
{
    public ParsedTsr3 Parse(StreamReader reader)
    {
        var standard = float.Parse(reader.ReadLine());
        var records = new List<Tsr3Record>();
        
        while (! reader.EndOfStream)
        {
            string? line = reader.ReadLine();

            if (line is not null)
            {
                var values = line.Split();


                var record = new Tsr3Record
                (
                    uuid: new Guid(values[0]),
                    name: $"{values[1]} {values[2][0]}.{values[3][0]}.",
                    debitingDate: Convert.ToDateTime(values[4]),
                    debitingHours: float.Parse(values[5], CultureInfo.InvariantCulture)
                );
                
                records.Add(record);
            }
        }

        return new ParsedTsr3(standard, records);
    }
}