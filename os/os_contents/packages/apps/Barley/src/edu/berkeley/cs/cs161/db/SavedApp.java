package edu.berkeley.cs.cs161.db;

public class SavedApp
{
	private String name;
	private String[] permissions;
	
	public SavedApp(String name, String[] permissions)
	{
		this.name = name;
		this.permissions = permissions;
	}

	public String[] getPermissions()
	{
		return permissions;
	}

	public void setPermissions(String[] permissions)
	{
		this.permissions = permissions;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

}
