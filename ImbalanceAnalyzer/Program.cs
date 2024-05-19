using System.Globalization;

namespace ImbalanceAnalyzer;
public class Program
{
    public static void Main(string[] args)
    {
        var readPath = Path.GetFullPath("./report.txt");
        var writePath = Path.GetFullPath("./result.txt");

        
        var parser = new Tsr3Parser();
        var compiler = new ResultCompiler();
        var resultWriter = new ResultWriter();

        using (StreamReader reader = new StreamReader(readPath))
        {
            var parsedFile = parser.Parse(reader);

            var result = compiler.Compile(parsedFile);

            using (StreamWriter writer = new StreamWriter(writePath, false))
            {
                resultWriter.Write(writer, result);
            }
        }
    }
}