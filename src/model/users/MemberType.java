package model.users;

public enum MemberType {
	
	REGULAR {
		@Override
		public String toString() {
			return "regular";
		}
	},
	SENIOR {
		@Override
		public String toString() {
			return "senior";
		}
	},
	CHILDREN {
		@Override
		public String toString() {
			return "children";
		}
	},
	STAFF {
		@Override
		public String toString() {
			return "staff";
		}
	},
	UNDEFINED {
		@Override
		public String toString() {
			return "undefined";
		}
	}

}
