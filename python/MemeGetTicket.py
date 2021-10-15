#!/usr/bin/python
# -*- coding: utf-8 -*-

import os
import jpype

def getBrTick():
    jvmPath = jpype.getDefaultJVMPath()
    # jpype.startJVM(jvmPath, "-ea", "-Djava.class.path=Encipher_Ticket.jar")
    jarpath = os.path.join(os.path.abspath('.'), "Encipher_Ticket.jar")
    jarpath2 = os.path.join(os.path.abspath('.'), "MemeAppScript-1.0-SNAPSHOT-jar-with-dependencies.jar")
    jpype.startJVM(jvmPath, "-ea", "-Djava.class.path=%s;%s" % (jarpath, jarpath2))
    JDClass = jpype.JClass("com.criag.Encipher")
    # 创建类实例对象
    jd = JDClass()
    # 引用jar包类中的方法 rsa_sign
    signature = jd.generateEncyptTicket()
    jpype.java.lang.System.out.println(signature)
    print(signature)
    MemeScriptClass = jpype.JClass("meme.MemeScript")
    MemeScript = MemeScriptClass()
    # MemeScript.main(MemeScript.getString(signature))
    jpype.shutdownJVM()
getBrTick()