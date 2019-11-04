import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;


public class Parser {

	public Parser() {
		
	}
	 public void CreateRepo() throws IOException
	 {
		//create Myrepo on the chosen destination folder
		
		File indexProperties = null;
		File manifestFile = null;
		File treeLogFile = null;
		Properties prop = new Properties();
		Properties treeProp = new Properties();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		do {
			String DirP = null;
			String DirR = null;
			String projectTreeLog = null;
			String manifestFilePath = null;
			System.out.println("Enter the Operation you want to perform");
			System.out.println("1. Create \n2. Check-in \n3. Check-Out \n4. Merge \n5. Exit");
			
			int choice = Integer.parseInt(br.readLine());
			
			//switch case
			switch (choice) {

			case 1:
				System.out.println("Enter the destination where you want to create the repository in the format C://... ");
				DirR = br.readLine();
				System.out.println("Enter the dirrectory where the project is located in the format C://... ");
				DirP =br.readLine();
		        File dir = new File(DirR+"/MyRepo");
				dir.mkdir();
		
				//create Project in Myrepo
				File ProjDir = new File(DirR+"/MyRepo/Project");
				ProjDir.mkdir();
				DirR = DirR+"/MyRepo/Project";
		
				//calling a method to parse through folders/files and create duplicate folders/files in repository
				Parser.CopyFolders(DirP,DirR);
				
				//System.out.println("All Folders Created inside repository");
				//calling a method to create a manifest file for all command functions
				manifestFilePath=Parser.createManifest("Create",DirP, DirR, Parser.filePropertiesList);
				//System.out.println("All Folders Created inside repository");
				String fileLabel = DirR+ "/" + "index-properties.txt";
				File fileLabelProp = new File(fileLabel);
				fileLabelProp.createNewFile();
				//System.out.println("All Folders Created inside repository");
				
				File projectTree = new File(DirP + "/" + "treeLog-properties.txt");
				projectTree.createNewFile();
				treeLogFile = initializeTreeLogFile(DirP); 
				treeProp.load(new FileInputStream(treeLogFile));
				manifestFile = new File(manifestFilePath);
				appendLogFile(treeLogFile,manifestFile.getName(),treeProp);
				System.out.println("All Folders Created inside repository");
				
				filePropertiesList.clear();
				break;
				
			case 2:

				try {

					System.out.println("Enter Source of the project "); // get source path from
														// user
					DirP = br.readLine();
					System.out.println("Enter Destination of the check- in"); // get destination
					DirR = br.readLine();
					//File Dirproj = new File(DirP);
					//File Dirrepo = new File(DirR);
					CopyFolders(DirP,DirR);
					
					manifestFilePath =Parser.createManifest("Check-In",DirP,DirR,Parser.filePropertiesList);
					
					System.out.println("Operation Done.");
					
					//indexProperties = initializeIndexFile(DirR);
					//prop.load(new FileInputStream(indexProperties));
					//labelOperation(indexProperties, prop, br, manifestFilePath);
					
					treeLogFile = initializeTreeLogFile(DirP);
					treeProp.load(new FileInputStream(treeLogFile));
					manifestFile = new File(manifestFilePath);
					appendLogFile(treeLogFile,manifestFile.getName(),treeProp);
					//kkakakakalidjeidjiciecjeicjeijceij
					
					
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Operation Failed."+e.getMessage());
				}
				Parser.filePropertiesList.clear();
							break;	
			case 3:
				File indexFile = null;
				String DirRepo = null;
				
				try {
					System.out.println("Enter the Path of the Repository starting with C:// : "); // get source path from user
					DirP = br.readLine();
					DirRepo = DirP;
					System.out.println("Do you want to select label? Y/N");
					char userChoice = br.readLine().charAt(0);
					indexFile = initializeIndexFile(DirP);
					Properties props = new Properties();
					props.load(new FileInputStream(indexFile));
					
					if (userChoice == 'Y' || userChoice == 'y') 
					{
						System.out.println("Enter Label Name");
						String userLabel = br.readLine();
						Enumeration enuKeys = props.keys();
						while (enuKeys.hasMoreElements()) 
						{
							String key = (String) enuKeys.nextElement();
							if (key.equals(userLabel)) 
							{
								DirP = props.get(userLabel).toString();
							}
						}
					} 
					else 
					{
						System.out.println("\nEnter the name of the Manifest File ");
						String manifestName = br.readLine();
						DirP += "/Manifest-log/"+manifestName;
						//sourceDir = Paths.get(DirP);
					}

					//sourceDir = Paths.get(DirP);

					System.out.println("Enter Destination"); // get destination path from user
																
					DirR = br.readLine();
					//destDir = Paths.get(DirR);

					
					Background.checkOutFileVersion(DirP, DirR);
					System.out.println("Operation Done.");
				
				
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Operation Failed.");
				}
				manifestFilePath =Parser.createManifest("Check-Out", DirP, DirR,
						Parser.filePropertiesList); // create Manifest file
													// containing details for
													// further use
				indexProperties = initializeIndexFile(DirRepo);
				prop.load(new FileInputStream(indexProperties));
				labelOperation(indexProperties, prop, br, manifestFilePath);
				
				projectTreeLog = DirR + "/" + "treeLog-properties.txt";
				projectTree = new File(DirR + "/" + "treeLog-properties.txt");
				projectTree.createNewFile();
				//treeLogFile = initializeTreeLogFile(projectTreeLog);
				//treeProp.load(new FileInputStream(treeLogFile));
				manifestFile = new File(manifestFilePath);
				//appendLogFile(treeLogFile,manifestFile.getName(),treeProp);
				//Parser.filePropertiesList;
				Parser.filePropertiesList.clear();
				break;
				
			case 4: try {

				System.out.println("Enter manifest filename :"); // get source path from
													// user
				DirP = br.readLine();
				

				System.out.println("Enter Current path :"); // get destination
															// path
															// from user
				DirR = br.readLine();
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Invalid path");
				System.exit(0);
			}
			
			Background.mergeTree(DirP, DirR);
			
		case 5:
				System.exit(0);
				break;
			default:
				System.out.println("Invalid Input");

			}

			} while (true);	
		}
	 
	 
	public static void labelOperation(File indexProperties, Properties prop, BufferedReader br, String manifestFilePath)
				throws IOException, FileNotFoundException {
			System.out.println("\nDo you want to label this Manifest File? : Y/N ");
			char userChoice = br.readLine().charAt(0);
			if (userChoice == 'Y' || userChoice == 'y') {
				System.out.println("\nEnter Label :");
				String label = br.readLine();
				
				prop.setProperty(label, manifestFilePath);
				FileOutputStream fileOut = new FileOutputStream(indexProperties);
				prop.store(fileOut, "index file to store labels");
				fileOut.flush();
				fileOut.close();
			}
		}

	 private static File initializeIndexFile(String sourceDirStr) {
			File indexFile = null;

			for (File f : new File(sourceDirStr).listFiles()) {
				if (f.isFile()) {
					if (f.getName().contains("index")) {
						indexFile = f;
					}
				}
			}
			return indexFile;
		}
	
public static List<FileProps> filePropertiesList = new ArrayList<FileProps>();
	

	
	 public static void CopyFolders(String Projectdirectory, String RepoDirectory) throws IOException
	 {
	        File Projdir = new File(Projectdirectory);
	        File Repodir = new File(RepoDirectory);
	        
	        //get all the files from a directory
	        File[] fList = Projdir.listFiles();
	        for (File file : fList)
	        {
	            if (file.isDirectory())
	            {
	                String Name = file.getName();
	                String FiledirrectoryRepo = Repodir+"/"+Name;
	                String FiledirrectlyProj=  Projdir+"/"+Name;
	                
	                //created new folders in MyRepo existing in Project
	                File dir = new File(Repodir+"/"+Name);
	                if (!dir.exists()) {
	        			dir.mkdir();}
	        			Parser.CopyFolders(FiledirrectlyProj,FiledirrectoryRepo);
	            }
	            else if (file.isFile()) 
	            {
	                String Name = file.getName();
	                
	                //created new folders in MyRepo existing in Project
	                File dir = new File(Repodir+"/"+Name);
	                if (!dir.exists()) {
	        			dir.mkdir();}
	                if (!(Name.contains("treeLog-properties.txt") || Name.contains(".DS_Store")))
	                {
	        			String FileDirProj =Projdir+"/"+Name+"/";
	        			String FileDirRepo =Repodir+"/"+Name+"/";
	        			File dirProj = new File(FileDirProj);
	        	        File dirRepo = new File(FileDirRepo);
	        	        Parser.ReadCopyFiles(dirProj,dirRepo);
	                }
	        	        
	            	}
	        }
	 }
	 
	 public static void ReadCopyFiles(File directoryName, File Directory) throws IOException 
	 {
		 //copying files with artifact id
		 File dirrepo= new File(Directory+"/"+Parser.checksum(directoryName));
		 if (!dirrepo.exists()) {
		 Files.copy(directoryName.toPath(), dirrepo.toPath());}
		 FileProps fileProp = new FileProps(dirrepo.getName(),directoryName.getName() ,dirrepo.getAbsolutePath(),directoryName.getAbsolutePath());
			filePropertiesList.add(fileProp);
	 }
	
	 
	 public static File checksum(File filepathProj) throws IOException 
	 {
		 	Scanner s = null;
			char[] myChar = null;
			try 
			{
				s = new Scanner(new BufferedReader(new FileReader(filepathProj)));
				String str = "";
				while (s.hasNext()) 
				{
					str += s.nextLine();
				}
				myChar = str.toCharArray();
			 } 
			finally 
			{
				if (s != null) 
				{
					s.close();
				}
			}
			
			int fileVersion = 0;
			for (int i = 0; i < myChar.length; i++) 
			{
				switch (i % 4)
				{
				case 0:
					fileVersion += (int) myChar[i] * 1;
					break;

				case 1:
					fileVersion += (int) myChar[i] * 7;
					break;

				case 2:
					fileVersion += (int) myChar[i] * 11;
					break;
					
				case 3:
					fileVersion += (int) myChar[i] * 17;
					break;
					
				default:
					break;
				}
			}
			//creating artifact id
			String fileName = fileVersion + "-L" + myChar.length;
			File targetFile = new File(fileName);
			return targetFile;
	 }
	 public static String createManifest(String command,String sourcePath, String repoPath, List<FileProps> fileProp) 
				throws IOException {
			
			ArrayList<String> lines = new ArrayList<String>();
			LocalDateTime currentDateTime = LocalDateTime.now();
			String timePath = String.valueOf(currentDateTime.getHour() + "." + currentDateTime.getMinute() + "."
					+ currentDateTime.getSecond() + " hrs");
			String fileName = "Manifest - " + currentDateTime.toLocalDate() + " - " + timePath + ".txt";
			Path manifestPath = FileSystems.getDefault().getPath(repoPath + FileSystems.getDefault().getSeparator()
					+ "Manifest-log" + FileSystems.getDefault().getSeparator() + fileName);
			Charset charset = StandardCharsets.US_ASCII;
			if (command.equalsIgnoreCase("Check-Out")) {
				String repositoryTemp = sourcePath;
				int indexOf= repositoryTemp.indexOf("Manifest-log");
				repositoryTemp = repositoryTemp.substring(0,indexOf-1);
				manifestPath = FileSystems.getDefault().getPath(repositoryTemp + FileSystems.getDefault().getSeparator()
						+ "Manifest-log" + FileSystems.getDefault().getSeparator() + fileName);
				verifyOrCreateActivityDir(repositoryTemp);
			} else if(command.equalsIgnoreCase("Create")){
				lines.add("Parent:Root");
				verifyOrCreateActivityDir(repoPath);
		}
			else if(command.equalsIgnoreCase("Check-In"))
			{
			//String repoTemp = sourcePath;
			//int indexOf= repoTemp.indexOf("Desktop");
				//repoTemp = repoTemp.substring(0,indexOf);
			//lines.add("Parent:" + getParentManifestFileName(repoPath+"/Manifest-log/", sourcePath, repoPath));
			verifyOrCreateActivityDir(repoPath);
		}
			
			Files.createFile(manifestPath);
			lines.add(fileName.split(".txt")[0]);
			lines.add("Command: " + command);
			lines.add("Source Path: "+ sourcePath);
			lines.add("Target Path: "+ repoPath);
			lines.add("Files created: ");
			lines.add("Version\t" + "\t File Name \t" + "\tFile Path"+"\t\t\t\tSource Path");
			if(!fileProp.isEmpty()) {
			fileProp.forEach((newfileProp) -> {
				String line = newfileProp.getArtifactID() + "\t" + newfileProp.getOldFileName() + "\t"
						+ newfileProp.getNewfilePath() + "\t" + newfileProp.getOldfilePath();
				lines.add(line);
			});
			} else {
				lines.add("No File Created");
			}
			
			Files.write(manifestPath, lines, charset, StandardOpenOption.APPEND);
			
			return manifestPath.toString();
	 }
	 public static String getParentManifestFileName(String dirPath,String sourcePath, String targetPath) throws IOException{
			File dir = new File(dirPath);
		    File[] files = dir.listFiles();
		    for (int i = 0; i < files.length; i++) {
		    	try {
		    		BufferedReader reader = new BufferedReader(new FileReader(files[i].getAbsolutePath()));
					ArrayList<String> list = new ArrayList<String>();
					int j = 0;
					while (reader.ready() && j< 6){
						j++;
					    list.add(reader.readLine());
					}
					if((list.get(4).contains(sourcePath) && list.get(5).contains(targetPath) && list.get(3).substring(9).equalsIgnoreCase("check-in"))  ||( list.get(4).contains(sourcePath) && list.get(5).contains(targetPath) && list.get(3).substring(9).equalsIgnoreCase("create") )){ //true for scenarios where parent is create or check-in 
						return files[i].getName();
					}else if(list.get(4).contains(targetPath) && list.get(5).contains(sourcePath) && list.get(3).substring(9).equalsIgnoreCase("check-out")){
						return files[i].getName();
					}
					reader.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
		    }
			return null;
		}
	 public static void verifyOrCreateActivityDir(String pathToRepo) throws IOException {
			Path path = FileSystems.getDefault()
					.getPath(pathToRepo + FileSystems.getDefault().getSeparator() + "Manifest-log");
			if (!Files.exists(path)) {
				Files.createDirectory(path);
			}

		}
	 public static void appendLogFile(File treeLogFile,String manifestFilePath,Properties treeProp)
				throws IOException, FileNotFoundException {
				
			  BufferedReader reader = new BufferedReader(new FileReader(treeLogFile));
			  int lines = 0;
			  while (reader.readLine() != null) 
				  lines++;
			  reader.close();
			  if(lines>0)
				  lines = lines-1;
			  else
				  lines = 1;
			  
			  	String noOfLinesString =""+lines;
			  	treeProp.setProperty(noOfLinesString,manifestFilePath);
				FileOutputStream fileOut = new FileOutputStream(treeLogFile);
				treeProp.store(fileOut,"");
				fileOut.flush();
				fileOut.close();
			}
	 public static File initializeTreeLogFile(String sourceDirStr) {
			File treeLogFile = null;

			for (File f : new File(sourceDirStr).listFiles()) {
				if (f.isFile()) {
					if (f.getName().contains("treeLog")) {
						treeLogFile = f;
					}
				}
			}
			return treeLogFile;
		}
	 public static void main(String[] args) throws IOException 
	 {
		// TODO Auto-generated method stub
		try 
		{
		Parser listFilesUtil = new Parser();
		listFilesUtil.CreateRepo();		  
		}
		catch(Exception e) 
		{
			System.out.println(e.getMessage());
		}
		
	}

}
