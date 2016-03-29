/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import search.ESearch1;



public class Esearch1_Servlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String query=request.getParameter("ekw");
        
       
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title></title>"); 
            out.println("<link rel='stylesheet' href='result_temp.css' type='text/css'>");
            out.println("</head>");
            out.println("<body>");
            insertsearchBox(out);
            //out.println("<h3>Search for \""+query+"\" got the following results. </h3>");
            
            String filepath=getServletContext().getRealPath("/WEB-INF/subtopics.txt");
            //search
            
            
            try
            {
                
               ESearch1.find(URLEncoder.encode(query),out,filepath); 
            }
            
            catch(Exception e){}
            
            
            
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>


private void insertsearchBox(PrintWriter out) {
        
        out.println("<form name=\"f3\"  method=\"post\" action=\"multiple\"><input type=\"search\" "
                + "placeholder='Search for term' name=\"query\"  size=\"50\">"
                + "<input class=\"search_button\" type=\"submit\" name=\"command\" value=\"Search\" /></form>");
           }
}

       
    

    