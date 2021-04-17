package com.elondregan.model;

public class InsuranceToUserId {
    String insurance;
    String userId;

    public InsuranceToUserId(String insurance, String userId){
        this.insurance = insurance;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o){
        if(o == this) return true;
        if(o == null || getClass() != o.getClass()) return false;
        InsuranceToUserId comparer = (InsuranceToUserId) o;
        if(!comparer.insurance.equals(this.insurance)) return false;
        if(!comparer.userId.equals(this.userId)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        int result = 1;
        result = hash * result + ((this.insurance == null) ? 0 : this.insurance.hashCode())
                + ((this.userId == null) ? 0 : this.userId.hashCode());
        return result;
    }
}
