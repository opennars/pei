package com.googlecode.opennars.parser.loan.Loan.Absyn; // Java Package generated by the BNF Converter.

public class URIFul extends URIRef {
  public final String urilit_;

  public URIFul(String p1) { urilit_ = p1; }

  public <R,A> R accept(com.googlecode.opennars.parser.loan.Loan.Absyn.URIRef.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof com.googlecode.opennars.parser.loan.Loan.Absyn.URIFul) {
      com.googlecode.opennars.parser.loan.Loan.Absyn.URIFul x = (com.googlecode.opennars.parser.loan.Loan.Absyn.URIFul)o;
      return this.urilit_.equals(x.urilit_);
    }
    return false;
  }

  public int hashCode() {
    return this.urilit_.hashCode();
  }


}
