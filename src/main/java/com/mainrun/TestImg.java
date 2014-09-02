package com.mainrun;

import java.awt.FlowLayout;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.commons.ConnectionFactory;
import com.dto.TestBlob;

public class TestImg extends JFrame
{

	/**
	 * *
	 */

	private static final long serialVersionUID = 1L;

	private ImageIcon icon;
	private JLabel lbImg, lbTxt;

	public TestImg()
	{
		try
		{
			Connection conn = ConnectionFactory.getConnection();
			String sql = "SELECT ID, PIC, TXT FROM TB_LOB WHERE ID=2";
			PreparedStatement pstm = conn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			TestBlob tb = new TestBlob();
			if (rs.next())
			{
				Blob pic = rs.getBlob("pic");
				byte[] picBytes = pic.getBytes(1, (int) pic.length());

				Blob txt = rs.getBlob("txt");
				byte[] txtBytes = txt.getBytes(1, (int) txt.length());

				tb.setId(rs.getInt("id"));
				tb.setPicBytes(picBytes);
				tb.setTxtBytes(txtBytes);
			}

			// TODO Auto-generated constructor stub
			this.setTitle("读取大对象");
			icon = new ImageIcon(tb.getPicBytes());
			lbImg = new JLabel(icon);
			lbTxt = new JLabel(new String(tb.getTxtBytes()));

			this.setLayout(new FlowLayout());
			this.add(lbImg);
			this.add(lbTxt);
			this.setSize(1100, 820);
			this.setVisible(true);

		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public static void main(String[] args)
	{
		TestImg ti = new TestImg();
	}
}
