import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;



public class Background {
	public static void mergeTree(String repositoryPath, String treePath) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(repositoryPath));
		int lines = 0;
		while (br.readLine() != null)
			lines++;
		
		List<String> fileData = new ArrayList<String>();
		for (int i = 0; i < lines; i++) {
			if (i >=6) {
				String line1 = Files.readAllLines(Paths.get(repositoryPath)).get(i);
				fileData.add(line1);

			}
		}
		// repository path contains mainfest filename; treepath is current directory
		
		List<File> fileList = new ArrayList<File>();
		List<File> resultList = new ArrayList<File>();
		fileList = listf(treePath, resultList);

		String command = Files.readAllLines(Paths.get(repositoryPath)).get(1);
		String[] commandSplit = command.split("\\s+");
		command = commandSplit[1];
		int numberOfFilesinProjectTree = fileList.size();// check if fileList equals number of files specified in
															// manifest file

		if (fileData.size() <= numberOfFilesinProjectTree) { // if equals then
															 // no new files
			// were added but files were
			// modified
			System.out.println("No files were added");
			if (command.equalsIgnoreCase("check-in") || command.equalsIgnoreCase("create")) {
				String sourceRoot = Files.readAllLines(Paths.get(repositoryPath)).get(2);
				String[] splitSourceRoot = sourceRoot.split("\\s+");
				sourceRoot = splitSourceRoot[2];
				for (String e : fileData) {
					String[] splitStr = e.split("\\s+");
					String artifactId = splitStr[0];
					String repoFilePath = splitStr[2];
					String sourceFilePath = splitStr[3];
					String originalFileName = splitStr[1];
					copyAndRenameFile(sourceRoot, sourceFilePath, repoFilePath, originalFileName, artifactId, fileList,
							treePath);

				}
			} else if (command.equalsIgnoreCase("check-out")) {
				String sourceRoot = Files.readAllLines(Paths.get(repositoryPath)).get(4);
				String[] splitSourceRoot = sourceRoot.split("\\s+");
				sourceRoot = splitSourceRoot[2]; 
				for (String e : fileData) {
					String[] splitStr = e.split("\\s+");
					String artifactId = splitStr[0];
					String repoFilePath = splitStr[2]; 
					String sourceFilePath = splitStr[3]; 
					String originalFileName = splitStr[1];

					copyAndRenameFile(sourceRoot, repoFilePath, sourceFilePath, originalFileName, artifactId, fileList,
							treePath);

				}

			}
			
			String treeLogPath = treePath + "/" + "treeLog-properties.txt";
			File treeLog = new File(treeLogPath);
			Properties props = new Properties();
			props.load(new FileInputStream(treeLog));
			int indexOf = repositoryPath.indexOf("Manifest");
			
			String activityLogFilesPath = repositoryPath.substring(0, indexOf-1);
			File activityLogFile = new File(activityLogFilesPath);
			
			Enumeration enuKeys = props.keys();
			//int i=0;
			List<String> treeLogList = new ArrayList<String>();
			File[] filesList = activityLogFile.listFiles();
			
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				treeLogList.add(props.get(key).toString());
			}
			 int j=0;
			for(int  k = filesList.length; k>0; k++) {
				File f = filesList[k];
				if(f.getName().equalsIgnoreCase(treeLogList.get(j))) {
					
				}
			}
			
			
		} else if (fileData.size() > numberOfFilesinProjectTree) {// else new
																// file was
			// added
			// then we have to copy file from repository to Project Tree
			System.out.println("Files were added");
			// List<File> filesToBeAdded = new ArrayList<File>();
			if (command.equalsIgnoreCase("check-in") || command.equalsIgnoreCase("create")) {
				String sourceRoot = Files.readAllLines(Paths.get(repositoryPath)).get(2);
				String[] splitSourceRoot = sourceRoot.split("\\s+");
				sourceRoot = splitSourceRoot[2];
				for (String e : fileData) {
					String[] splitStr = e.split("\\s+");
					String sourceFilePath = splitStr[3];
					String repoFilePath = splitStr[2];
					String originalFileName = splitStr[1];
					String artifactId = splitStr[0];
					String repoProjectFilePath = sourceFilePath.substring(sourceRoot.length());
					String checkOutRepoFilePath = treePath + repoProjectFilePath;
					File checkFile = new File(checkOutRepoFilePath);
					if (fileList.contains(checkFile)
							|| fileList.contains(new File(checkFile.getAbsolutePath() + "_MT"))) {

						System.out.println("File is present");
					} else {
						// copy file pointed by sourceFilePath to that directory
						System.out.println("File Added : " + checkFile.getName());
						File myFile = new File(checkOutRepoFilePath);
						File RepoFile = new File(repoFilePath);
						// Now get the path
						File parentFile = new File(myFile.getParent()); 
						Files.copy(parentFile.toPath(), RepoFile.toPath());
						;
						File oldName = new File(parentFile.getAbsolutePath() + "/" + artifactId);
						File newName = new File(parentFile.getAbsolutePath() + "/" + originalFileName);
						oldName.renameTo(newName);
					
					}
				}
			}
		}
	}
	
	
 
 public static String getExtension(String fileName) {
		String extension = null;
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i + 1);
		}
		return extension;
	}
 public static void checkOutFileVersion(String manifestFilePath,String targetPath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(manifestFilePath));
		int lines = 0;
		while (br.readLine() != null) lines++;
		List<String> fileList = new ArrayList<String>();
		for(int i=0;i<lines ; i++){
			if(i>6){
			String line1 = Files.readAllLines(Paths.get(manifestFilePath)).get(i);
			fileList.add(line1);
			
		}
			}
		String checkOutSourceRoot = Files.readAllLines(Paths.get(manifestFilePath)).get(4);
		String[] splitCheckOutSourceRoot = checkOutSourceRoot.split("\\s+");
		checkOutSourceRoot =  splitCheckOutSourceRoot[2];
		//String rootFilePath= null;
		System.out.println("checkout src root"+checkOutSourceRoot);
		for(String e : fileList){
			String[] splitStr = e.split("\\s+");
			String originalFileName = splitStr[1];
			String sourceFilePath = splitStr[2];
			String artifactId = splitStr[0];
			String rootFilePath = splitStr[2].substring(checkOutSourceRoot.length());
			System.out.println("sourcePath: "+ sourceFilePath);
			//System.out.println("rootfile"+rootFilePath);
			String destPath = targetPath  + rootFilePath;
			destPath = destPath.replace("/" + originalFileName, "");
			destPath = destPath.replace("/" + artifactId, "");
			
			System.out.println("Destination Path : "+destPath);
			
			CopyFile(sourceFilePath, destPath, originalFileName);
			destPath = targetPath  + rootFilePath;
			
			//destPath += "/"+artifactId;
			CopyFile(sourceFilePath, destPath, originalFileName);
		}
	}
 public static void CopyFile(String sourcePath, String targetPath, String originalFileName) {
		File targetFile = new File(targetPath);
		//File sourceFile = new File(sourcePath);
		if (!targetFile.exists() || !targetFile.isFile()) {
			targetFile.mkdir();
		}
		else {
			//targetPath+="/"+originalFileName;
			//targetFile = new File(targetPath);
		File sourceFile = new File(sourcePath);
		try {
			
			Files.copy(sourceFile.toPath(), targetFile.toPath());
			File oldName = new File(targetFile + "/" + sourceFile.getName());
			File newName = new File(targetFile + "/" + originalFileName);
			oldName.renameTo(newName);
			FileProps fileProp = new FileProps(oldName.getName(), newName.getName(),newName.getAbsolutePath(), sourceFile.getAbsolutePath());
			Parser.filePropertiesList.add(fileProp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	public static boolean copyAndRenameFile(String sourceRoot, String sourceFilePath, String repoFilePath,
			String originalFileName, String repoArtifactId, List<File> fileList, String treePath) throws IOException {

		for (File f : fileList) {
			String filePath = f.getAbsolutePath();
			String projectFilePath = null;
			String repoProjectFilePath = null;
			boolean isFileNotRenamed = true;
			if (filePath.contains("_MT")) {
				filePath = filePath.substring(0, filePath.length() - 3);
				isFileNotRenamed = false;
			}
		
			projectFilePath = filePath.substring(treePath.length());

			repoProjectFilePath = sourceFilePath.substring(sourceRoot.length());
			
			System.out.println(projectFilePath);
			System.out.println(repoProjectFilePath);

			if (projectFilePath.equalsIgnoreCase(repoProjectFilePath) && isFileNotRenamed) {
				String artifactId = Parser.checksum(f).getName() + "." + getExtension(f.getAbsolutePath());
				if (repoArtifactId.equals(artifactId)) {
					// file has not been modified
					System.out.println("File was not modified");
				} else {
					System.out.println("File was modified");
					File sourceFile = new File(repoFilePath);
					projectFilePath = treePath
							+ projectFilePath.substring(0, projectFilePath.length() - originalFileName.length());
					
					File treeFile = new File(projectFilePath);
					try {
						Files.copy(sourceFile.toPath(), treeFile.toPath());
						File oldName = new File(projectFilePath + "/" + sourceFile.getName());
						int indexOf = originalFileName.indexOf(".");
						File newName = new File(projectFilePath + "/" + originalFileName.substring(0, indexOf) + "_MR" + originalFileName.substring(indexOf, originalFileName.length())); // rename
																									// MR
						oldName.renameTo(newName);
						File oldName1 = new File(projectFilePath + "/" + originalFileName);
						File newName1 = new File(projectFilePath + "/" + originalFileName.substring(0, indexOf) + "_MT" + originalFileName.substring(indexOf, originalFileName.length()));
						oldName1.renameTo(newName1);
					} 
					catch (Exception e) 
					{
						e.getMessage();
					}
				}
			}
		}
		return false;
	}
	public static List<File> listf(String directoryName, List<File> resultList) {
		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				String fileExtension = getExtension(file.getAbsolutePath());
				if (fileExtension.contains("_MR") || fileExtension.contains("_MG"))
					System.out.println("MR or MG File. Not Added");
				else
					resultList.add(file);
			} else if (file.isDirectory()) {
				listf(file.getAbsolutePath(), resultList);
			}
		}
		// System.out.println(fList);
		return resultList;
	}
	public static int listf(String directoryName) {
		File directory = new File(directoryName);
		int countNumberOfMT_Files = 0;
		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				String fileExtension = getExtension(file.getAbsolutePath());
				if (fileExtension.contains("_MT")) {
					countNumberOfMT_Files++;
				}
			} else if (file.isDirectory()) {
				listf(file.getAbsolutePath());
			}
		}

		return countNumberOfMT_Files;
	}

}
