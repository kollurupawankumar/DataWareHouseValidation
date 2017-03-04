package com.pawan.gzip.filesystem;

import java.io.IOException;

import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;

public class PawanFileSystem extends LocalFileSystem{
	
	public PawanFileSystem() {
		super();
	}
	
	@Override
	public boolean mkdirs(Path f, FsPermission permission) throws IOException {
		final boolean result = super.mkdirs(f);
		this.setPermission(f, permission);
		return result;
	}
	
	@Override
	public void setPermission(Path p, FsPermission permission) throws IOException {
		try {
			super.setPermission(p, permission);
		} catch (Exception e) {
		}
	}

}
