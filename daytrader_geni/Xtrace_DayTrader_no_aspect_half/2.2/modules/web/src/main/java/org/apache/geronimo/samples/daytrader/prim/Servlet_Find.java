/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.samples.daytrader.prim;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.geronimo.samples.daytrader.beans.AccountDataBean;
import org.apache.geronimo.samples.daytrader.beans.AccountProfileDataBean;

/**
 * Servlet implementation class Servlet_Find
 */

public class Servlet_Find extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// gerome
	@PersistenceUnit
	private EntityManagerFactory emf;

	@Resource
	// private javax.transaction.UserTransaction transaction;
	private EntityTransaction et;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Servlet_Find() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("gerome doget");
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		display(request, response);
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("gerome service");
		doPost(request, response);

	}

	public void display(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		EntityManager entityManager;
		emf = Persistence.createEntityManagerFactory("daytrader");
		entityManager = emf.createEntityManager();
		
		AccountProfileDataBean profile = entityManager.find(
				AccountProfileDataBean.class, "uid:0");
		
		Query query = entityManager.createNamedQuery("holdingejb.holdingsByUserID");
		query.setParameter("userID", "uid:0");

	}

	public void find(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		EntityManager entityManager;
		emf = Persistence.createEntityManagerFactory("daytrader");

		entityManager = emf.createEntityManager();
		// transaction = UserTransaction.class.cast(new InitialContext()
		// .lookup("java:comp/UserTransaction"));
		et = entityManager.getTransaction();

		// AccountProfileDataBean profile = entityManager.find(
		// AccountProfileDataBean.class, "uid:0");
		//
		// AccountDataBean profile1 = entityManager.find(AccountDataBean.class,
		// 999999);

		AccountProfileDataBean profile;

		AccountDataBean profile1;
		profile = new AccountProfileDataBean("uid:0", "uid:0", "xxx", "xxx",
				"xxx@ibm.com", "xxx");
		profile1 = new AccountDataBean(0, 0, null, new Timestamp(System
				.currentTimeMillis()), new BigDecimal(3), new BigDecimal(3),
				"uid:0");
		profile.setAccount(profile1);
		profile1.setProfile(profile);
		// transaction.begin();
		et.begin();

		entityManager.persist(profile);

		entityManager.persist(profile1);
		try {
			// transaction.commit();
			et.commit();

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		entityManager.close();

		out.write("geromepost" + profile.getAddress());

	}
}
