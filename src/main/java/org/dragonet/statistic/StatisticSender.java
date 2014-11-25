/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */

package org.dragonet.statistic;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class StatisticSender {
  public static final String SERVER_URL = "http://stats.dragonet.org/counter.php";
  private String version;
  private long startupTime;
  
  public StatisticSender(String version, long startupTime)
  {
    this.version = version;
    this.startupTime = startupTime;
  }
  
  public void sendStatistic()
  {
    try
    {
      String queryURL = null;
      queryURL = "http://stats.dragonet.org/counter.php?version=" + URLEncoder.encode(this.version, "UTF-8") + "&startupTime=" + this.startupTime;
      HttpURLConnection conn = (HttpURLConnection)new URL(queryURL).openConnection();
      conn.setRequestMethod("GET");
      conn.setConnectTimeout(5000);
      conn.setDoOutput(false);
      conn.setDoInput(true);
      conn.connect();
      conn.getInputStream().close();
      conn.disconnect();
    }
    catch (Exception ex)
    {
      return;
    }
  }
}
