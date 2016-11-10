package GlobalPkg;

public class UserInfo {
	public String strUsername;
	public long lUID;
	public boolean bIsOurUser;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((strUsername == null) ? 0 : strUsername.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserInfo other = (UserInfo) obj;
		if (strUsername == null) {
			if (other.strUsername != null)
				return false;
		} else if (!strUsername.equals(other.strUsername))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return strUsername;
	}

}
