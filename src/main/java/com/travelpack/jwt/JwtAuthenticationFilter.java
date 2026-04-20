package com.travelpack.jwt;



import java.io.IOException;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    @Autowired
    private JwtAuthenticationHelper jwtHelper;

    @Autowired
    UserDetailsService userDetailsService;

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//
//        String requestHeader = request.getHeader("Authorization");
//
//        //Bearer yeybaggiwjsbshdhddhf
//        String username =null;
//        String token =null;
//        if(requestHeader!=null && requestHeader.startsWith("Bearer"))
//        {
//            token = requestHeader.substring(7);
//
//            username= jwtHelper.getUsernameFromToken(token);
//
//            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
//            {
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//                if(!jwtHelper.isTokenExpired(token))
//                {
//                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
//                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//                }
//
//            }
//        }
//        filterChain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            token = requestHeader.substring(7);

            try {
                username = jwtHelper.getUsernameFromToken(token);
            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                // 1. Catch the specific expiry exception
                sendUnauthorizedResponse(response, "JWT Token has expired");
                return; // 2. STOP the filter chain here
            } catch (Exception e) {
                // Catch other JWT issues (malformed, signature mismatch, etc.)
                sendUnauthorizedResponse(response, "Invalid JWT Token");
                return;
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (!jwtHelper.isTokenExpired(token)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    // 3. Helper method to write the 401 JSON response
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType("application/json");
        String body = String.format("{\"status\": 401, \"error\": \"Unauthorized\", \"message\": \"%s\"}", message);
        response.getWriter().write(body);
    }

}


