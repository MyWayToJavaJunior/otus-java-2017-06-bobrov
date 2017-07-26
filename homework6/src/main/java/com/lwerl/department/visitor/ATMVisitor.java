package com.lwerl.department.visitor;

import com.lwerl.department.ATMInfo;

public interface ATMVisitor {

    void visit(ATMInfo atmInfo);
}
