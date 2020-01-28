using WPFTools;
using WPFTools.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using WorkManager.Data.Enums;

namespace WorkManager.Data.Models
{
    public class DataContext : EFContext
    {
        public DbSet<Project> Projects { get; set; }
        public DbSet<Task> Tasks { get; set; }
        public DbSet<TaskTime> TaskTimes { get; set; }

        public DbSet<Resource> Resources { get; set; }
        public DbSet<ProjectResource> ProjectResources { get; set; }
        public DbSet<TaskResource> TaskResources { get; set; }
        public DbSet<Team> Teams { get; set; }
        public DbSet<TeamAccount> TeamAccounts { get; set; }

        public DbQuery<V_Resource> V_Resources { get; set; }
        public DbQuery<V_Team> V_Teams { get; set; }
        public DbQuery<V_Project> V_Projects { get; set; }
        public DbQuery<V_Task> V_Tasks { get; set; }
        public DbQuery<EFValuesHistory> EFValuesHistory { get; set; }
        public DbQuery<V_ProjectStat> V_ProjectsStat { get; set; }
        public DbQuery<V_AccountStat> V_AccountStat { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<TaskTime>(entity =>
            {
                entity.ToTable("TASKTIMES");
                entity.Property(x => x.Type).HasConversion(x => (int)x, x => (TimeType)x);
            });
            modelBuilder.Entity<Task>(entity =>
            {
                entity.Property(x => x.State).HasConversion(x => (int)x, x => (TaskState)x);
            });
            modelBuilder.Query<V_ProjectStat>(entity =>
            {
                entity.Property(x => x.State).HasConversion(x => (int)x, x => (TaskState)x);
            });
            modelBuilder.Query<EFValuesHistory>(entity =>
            {
                entity.Property(x => x.ModType).HasConversion(x => (byte)x, x => (HistoryModificationType)x);
            });
            base.OnModelCreating(modelBuilder);
        }
        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
                optionsBuilder.UseSqlServer(Config.ConnectionString);
                optionsBuilder.UseQueryTrackingBehavior(QueryTrackingBehavior.NoTracking);
            }
            base.OnConfiguring(optionsBuilder);
        }
    }
}
