import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class sort_it {
	private static boolean direction = true; // true - ascending, false - descending
	private static boolean dataType;		 // true - integer,   false - string
	private static String outFile; 
	private static List<List<String>> fullList = new ArrayList<List<String>>();
	private static List<String> result = new ArrayList<String>();
	private static boolean input_process(String[] args)
	{
		dataType = true;	
		outFile = "out.txt";
		List<String> fileNames = new ArrayList<String>();
		boolean args_success = false;

		//  [-i -s] [-a -d]? [out.txt] [in.txt]+
		if (args.length < 3)
		{
			System.out.println("Error. There have to be 3 parameters at least");
			return false;
		}
		for (int i = 0, state = 0; i < args.length; i++, state++)
		{
			if (state == 0)
			{
				if (args[i].equals("-i"))
					dataType = true;
				else if (args[i].equals("-s"))
					dataType = false;
				else
				{
					System.out.println("Error. Incorrect dataType parameter. Please specify -i for integer or -s for string");
					return false;
				}
				continue;
			}
			if (state == 1)
			{
				if (args[i].equals("-a"))
					direction = true;
				else if (args[i].equals("-d"))
					direction = false;
				else
					state++;
			}
			if (state == 2)
			{
				outFile = args[i];
				continue;
			}
			if (state == 3)
			{
				fileNames.add(args[i]);
				state--;
				args_success = true;
				continue;
			}
		}
		if (!args_success)
		{
			System.out.println("Error. Wrong parameters overall.");
			return false;
		}

		List<String> lst = new ArrayList<>();
		for (int i = 0; i < fileNames.size(); i++)
		{
			try {
				lst = Files.readAllLines(new File(fileNames.get(i)).toPath());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Reading file error");
				return false;
			}
			fullList.add(lst);
		}
		return true;
	}
	private static void sort()
   	{
		String base = "", current = "", most_value = "", last_value = "";
		int most_index, i;
		most_index = 0;
		most_value = fullList.get(0).get(0);
		while (fullList.size() > 0)
		{
			for (i = 0; i < fullList.size(); i++)
			{
				if (fullList.get(i).size() > 0)
				{
					current = fullList.get(i).get(0);
					if (current.indexOf(' ') != -1)
                    {
                        fullList.get(i--).remove(0);
                        continue;
                    }
					if (i == 0)
					{
						most_index = 0;
						most_value = current;
					}
					if (dataType)
					{
						if (direction)
						{
						    if (result.size() > 0 && Integer.parseInt(current) < Integer.parseInt(last_value))
                            {
                                fullList.get(i--).remove(0);
                                continue;
                            }
							if (Integer.parseInt(current) < Integer.parseInt(most_value))
							{
								most_index = i;
								most_value = current;	
							}
						}
						else
						{
                            if (result.size() > 0 && Integer.parseInt(current) > Integer.parseInt(last_value))
                            {
                                fullList.get(i--).remove(0);
                                continue;
                            }
							if (Integer.parseInt(current) > Integer.parseInt(most_value))
							{
								most_index = i;
								most_value = current;	
							}							
						}
					}
					else	
					{
						if (direction)
						{
                            if (result.size() > 0 && current.compareTo(last_value) < 0)
                            {
                                fullList.get(i--).remove(0);
                                continue;
                            }
							if (current.compareTo(most_value) < 0)
							{
								most_index = i;
								most_value = current;	
							}
						}
						else
						{
                            if (result.size() > 0 && current.compareTo(last_value) > 0)
                            {
                                fullList.get(i--).remove(0);
                                continue;
                            }
							if (current.compareTo(most_value) > 0)
							{
								most_index = i;
								most_value = current;	
							}
						}
					}
				}
				else 
				{
					fullList.remove(i--);
				}
			}
			if (fullList.size() > 0 && fullList.get(most_index).size() > 0)
			{
				fullList.get(most_index).remove(0);
				result.add(most_value);
				last_value = most_value;
			}
		}

	}
	private static void output_process()
	{
		for (int i = 0; i < result.size(); i++)
			System.out.println(result.get(i));
		Path path = Paths.get(outFile);
        try {
            Files.write(path, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	public static void main(String[] args)
	{
		if (!input_process(args))
			return;
		sort();
		output_process();
	}
}
