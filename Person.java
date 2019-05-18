class Person implements Comparable <Person>  {
	String firstName;
	String lastName;
	String phoneNumber;
	 
	public Person() {
		firstName="Placeholder";
		lastName="Placeholder";
		phoneNumber="XXX-XXX-XXXX";
	}
	public String getFirstName()
	{

		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	public String getPhoneNumber()
	{
		return phoneNumber;
	}
	/*public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}*/
	@Override
	public String toString() {
		return ("Person:" + this.firstName + " " + this.lastName);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Person)) return false;
		
		final Person other = (Person) obj; //downcasting
		if(this.firstName != other.firstName) return false;
		return true;
	}
	
	@Override
	public int compareTo(Person o) {
		// TODO Auto-generated method stub
		return 0;
	}
	 
	//write getters & setters 
	
}